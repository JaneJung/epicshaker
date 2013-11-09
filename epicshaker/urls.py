from django.conf.urls import patterns, include, url

from epicshaker.controllers.beverages import list_beverages, get_beverage, add_beverage
from epicshaker.controllers.tests import test

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
    url(r'^$', list_beverages, name = 'list_beverages'),
    url(r'^recipe/', get_beverage, name = 'get_beverage'),
    url(r'^addrecipe/', add_beverage, name='add_beverage'),
)
