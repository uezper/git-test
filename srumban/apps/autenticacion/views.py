import json
from django.core.urlresolvers import reverse
from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from django.contrib.auth import authenticate as djAuthenticate
from django.contrib.auth import login as djLogin
from django.contrib.auth import logout as djLogout
from apps.autenticacion.decorators import login_required
#from autenticacion import urls

def login(request):
    """
    Retorna la vista correspondiente a la página de login.



    :param request: Los datos de la solicitud

    :returns: Un 'renderizado' del template correspondiente.

    """

    if (request.user.is_active):
        return HttpResponseRedirect(reverse('auth:app'))

    # For cookie-based sessions
    request.session.set_test_cookie()
    return render(request, 'login')

def authenticate_user(request):
    """
    Autentifica al par usuario:contraseña, vinculando la sesión con el usuario.
    Utiliza AJAX para recibir y responder las solicitudes.

    :param request: Solicitud AJAX con los datos del login.
    
    :returns:
        - Un JsonRequest con los campos 'message' y 'STATUS' cargados correspondientemente.
        - En caso de ``'STATUS' = 'OK'``, se logeo correctamente al usuario.
        - En caso de ``'STATUS' = 'ERROR'``, occurio un error que se describe en ``'message'``

    """
    #TODO Unify with the login view. Discriminate through request.method and request.is_ajax.
    
    # Container for the ajax response
    data = {} 
        
    if request.method == 'POST' and request.is_ajax():

        if request.is_ajax():
            
            if not request.session.test_cookie_worked():
                data['message'] = 'Necesito utilizar cookies!.'
                data['STATUS'] = 'ERROR'
                return JsonResponse(data)

            # TODO User a proper form! (When finish, remove import json)
            fromClient = json.loads(request.body.decode('utf-8'))
        
            if 'username' not in fromClient or 'password' not in fromClient:
                #TODO Fix this with a proper error! (Probably a manually build request)
                data['message'] = 'Sucedió algo inesperado!.'
                data['STATUS'] = 'ERROR'
                return JsonResponse(data)

            user = djAuthenticate(username = fromClient['username'], password = fromClient['password'])
            if user is not None:
                if user.is_active:
                    #TODO And if there is a request of login of a already loged in user?
                    djLogin(request, user)
                    data['message'] = 'Bienvenido! Redireccionandote...'
                    data['STATUS'] = 'OK'
                    return JsonResponse(data)

                else:
                    data['message'] = 'Cuenta desactivada.'
                    data['STATUS'] = 'ERROR'
                    return JsonResponse(data)
            
            else:
                data['message'] = 'Usuario y contraseña inválidos.'
                data['STATUS'] = 'ERROR'
                return JsonResponse(data)
        
    else:
        #TODO Fix this with a proper error! (Probably a manually build request)
        data['message'] = 'Sucedió algo inesperado!.'
        data['STATUS']  = 'ERROR'
        return JsonResponse(data)

def deauthenticate_user(request):
    """
    Desautentifica al usuario relacionado con la sesión de la solicitud.
    
    :param request: Los datos de la solicitud.
    
    :returns: Un *HttpResponseRedirect* a la página de logeo.

    """
    #TODO On logout, redirect to login?
    djLogout(request)
    return HttpResponseRedirect(reverse('auth:name'))

@login_required('auth:app')
def app(request):
    """
    Vista temporal, para simular la aplicación.
    """
    user = request.user
    return HttpResponse('Hi, {}! <a href={}>Logout</a>'.format(user.first_name, '/auth/deauthenticate_user/'))

@login_required('auth:app2')
def app2(request):
    """
    Vista temporal, para simular la aplicación.
    """
    return HttpResponse('Hi!, you\'re in a private area.')

def data(request):
    """
    Vista temporal, para simular la aplicación. Muestra los datos del usuario.
    """
    user = request.user
    return HttpResponse('You are {}'.format(user))
