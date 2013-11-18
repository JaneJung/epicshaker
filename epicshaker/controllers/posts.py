import json
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

from epicshaker.models.post import Post

def get_post(request):
    response_data = {}
    posts = []
    if 'beverage_id' in request.GET:
        berg_id = request.GET['beverage_id']
        posts = Post.query(Post.beverage_id == berg_id)

    for post in posts:
        data = {}
        data['comment'] = post.comment
        response_data[post.key.id()] = data

    return HttpResponse(json.dumps(response_data, indent=2), content_type="application/json")

@csrf_exempt
def add_post(request):
    
    if request.method != 'POST':
        return HttpResponse("should be post")

    if 'beverage_id' in request.POST and 'comment' in request.POST:
        post = Post(beverage_id = request.POST['beverage_id'],
                    comment = request.POST['comment'])

    post.put()
    return HttpResponse("completed")
