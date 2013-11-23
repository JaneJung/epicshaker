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
    berg_name = None
    berg_tags = None
    berg_ingres = None

    if 'name' in request.GET:
        berg_name = request.GET['name']
    if 'tags' in request.GET:
        tags = request.GET['tags']
        berg_tags = [x.strip() for x in tags.split(",")]
    if 'ingredients' in request.GET:
        ingres = request.GET['ingredients']
        berg_ingres = [x.strip() for x in ingres.split(",")]

    bergs = []
    if berg_name is not None and berg_tags is not None:
        bergs = Beverage.query(Beverage.name == berg_name,
                               Beverage.tags.IN(berg_tags))
    elif berg_name is not None:
        bergs = Beverage.query(Beverage.name == berg_name)
    elif berg_tags is not None:
        bergs = Beverage.query(Beverage.tags.IN(berg_tags))
    else:
        bergs = Beverage.query()

    for berg in bergs:
        if berg_ingres is not None:
            if not all (k in berg.recipe for k in berg_ingres):
                continue
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
        berg.tags = [x.strip() for x in tags.split(",")]

    if 'description' in request.POST:
        berg.description = request.POST['description']

    berg.put()
    return HttpResponse('completed')


