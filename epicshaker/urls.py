from django.conf.urls import patterns, include, url

from epicshaker.controllers.beverages import list_beverages, get_beverage, add_beverage
from epicshaker.controllers.ingredients import list_ingredients, get_ingredient
from epicshaker.controllers.posts import add_post, get_post

from epicshaker.controllers.tests import test, test2, test3, test4

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'epicshaker.views.home', name='home'),
    # url(r'^epicshaker/', include('epicshaker.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),

    url(r'^test/', test, name='test'),
    url(r'^intest/', test2, name='test2'),
    url(r'^imagetest/', test3, name='test3'),
    url(r'^querytest/', test4, name='test4'),

    url(r'^recipelist/$', list_beverages, name = 'list_beverages'),
    url(r'^recipe/', get_beverage, name = 'get_beverage'),
    url(r'^addrecipe/', add_beverage, name='add_beverage'),

    url(r'^ingredientlist/', list_ingredients, name = 'list_ingredients'),
    url(r'^ingredient/', get_ingredient, name = 'get_ingredient'),

    url(r'^addpost/', add_post, name = 'add_post'),
    url(r'^getpost/', get_post, name = 'get_post'),
)
