from django.http import HttpResponse

from epicshaker.models.menu import Beverage

def test(request):
    berg = Beverage(name='Ultimate Frozen Strawberry Margarita',
                    description='A near perfect strawberry margarita with frozen strawberries and limeade concentrate.',
                    image='http://images.media-allrecipes.com/userphotos/250x250/00/23/47/234705.jpg',
                    recipe={'tequila':170, 'triple sec':56,
                            'frozen sliced strawberries':226},
                    tags=['sweet', 'cool'])
    berg.put()
    return HttpResponse("Hello, world. You're at the beverage index.")
    
