import json
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

from epicshaker.models.menu import Beverage

def list_beverages(request):
    bergs = Beverage.query()
    response_data = {}
    for berg in bergs:
        data = {}
        data['name'] = berg.name
        data['description'] = berg.description
        data['image'] = berg.image
        data['recipe'] = berg.recipe
        data['tags'] = berg.tags
        response_data[berg.key.id()] = data

    return HttpResponse(json.dumps(response_data, indent=2), content_type="application/json")

def get_beverage(request):

    response_data = {}
    bergs = []
    if 'name' in request.GET:
        berg_name = request.GET['name']
        bergs = Beverage.query(Beverage.name == berg_name)
    elif 'tags' in request.GET:
        tags = request.GET['tags']
        berg_tags = tags.split(", ")
        bergs = Beverage.query(Beverage.tags.IN(berg_tags))
    elif 'ingredient' in request.GET:
        ingredient = request.GET['ingredient']
        all_bergs = Beverage.query()
        for berg in all_bergs:
            if ingredient in berg.recipe:
                bergs.append(berg)

    for berg in bergs:
        data = {}
        data['name'] = berg.name
        data['description'] = berg.description
        data['image'] = berg.image
        data['recipe'] = berg.recipe
        data['tags'] = berg.tags
        response_data[berg.key.id()] = data

    return HttpResponse(json.dumps(response_data, indent=2), content_type="application/json")

@csrf_exempt
def add_beverage(request):

    if request.method != 'POST':
        return HttpResponse("should be post")

    if 'name' not in request.POST:
        return HttpResponse("name field is empty")
    if 'recipe' not in request.POST:
        return HttpResponse("name field is empty")

    berg = Beverage(name = request.POST['name'],
                    recipe = json.loads(request.POST['recipe']))

    if 'tags' in request.POST:
        tags = request.POST['tags']
        berg.tags = tags.split(", ")

    if 'description' in request.POST:
        berg.description = request.POST['description']

    berg.put()
    return HttpResponse('completed')


