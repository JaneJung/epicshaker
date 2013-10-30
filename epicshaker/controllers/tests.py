from django.http import HttpResponse

from epicshaker.models.menu import Beverage

def test(request):
    berg = Beverage(name='abc',
                    recipe={'a':10, 'b':20},
                    tags=['cool'])
    berg.put()
    return HttpResponse("Hello, world. You're at the beverage index.")
    
