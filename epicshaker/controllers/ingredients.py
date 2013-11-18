import json
from django.http import HttpResponse

from epicshaker.models.ingredient import Ingredient

def list_ingredients(request):
    ingrs = Ingredient.query()
    response_data = {}
    for ingr in ingrs:
        data = {}
        data['name'] = ingr.name
        response_data[ingr.key.id()] = data

    return HttpResponse(json.dumps(response_data, indent=2), content_type="application/json")

def get_ingredient(request):
    response_data = {}
    ingrs = []
    if 'name' in request.GET:
        ingr_name = request.GET['name']
        ingrs = Ingredient.query(Ingredient.name == ingr_name)

    for ingr in ingrs:
        data = {}
        data['name'] = ingr.name
        # data['image'] = ingr.image
        response_data[ingr.key.id()] = data

    return HttpResponse(json.dumps(response_data, indent=2), content_type="application/json")
