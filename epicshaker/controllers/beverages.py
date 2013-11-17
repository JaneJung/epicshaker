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
    if 'tags' in request.GET:
        berg_tags = request.GET.getlist('tags')
        bergs = Beverage.query(Beverage.tags.IN(berg_tags))

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

    if 'name' in request.POST and 'recipe' in request.POST:
        berg = Beverage(name = request.POST['name'],
                        recipe = json.loads(request.POST['recipe']))

    berg.put()
    return HttpResponse("completed")


