from django.http import HttpResponse

from epicshaker.models.menu import Beverage
from epicshaker.models.ingredient import Ingredient

def test(request):
    berg = Beverage(name='Ultimate Frozen Strawberry Margarita',
                    description='A near perfect strawberry margarita with frozen strawberries and limeade concentrate.',
                    image='http://images.media-allrecipes.com/userphotos/250x250/00/23/47/234705.jpg',
                    recipe={'tequila':170, 'triple sec':56,
                            'frozen sliced strawberries':226},
                    tags=['sweet', 'cool'])
    berg.put()
    return HttpResponse("Hello, world. You're at the beverage index.")
    
def test2(request):
    ingr1 = Ingredient(name = 'vodka')
    ingr1.put()

    ingr2 = Ingredient(name = 'pineapple juice')
    ingr2.put()

    ingr3 = Ingredient(name = 'coconut rum')
    ingr3.put()

    ingr4 = Ingredient(name = 'gin')
    ingr4.put()

    ingr5 = Ingredient(name = 'tequila')
    ingr5.put()

    ingr6 = Ingredient(name = 'triple sec')
    ingr6.put()

    ingr7 = Ingredient(name = 'fresh lime juice')
    ingr7.put()

    ingr8 = Ingredient(name = 'rum')
    ingr8.put()

    ingr9 = Ingredient(name = 'pineapple juice')
    ingr9.put()

    ingr10 = Ingredient(name = 'coconut milk')
    ingr10.put()

    return HttpResponse("Hello, world. You're at the ingrediant index.")
