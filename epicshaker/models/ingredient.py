from google.appengine.ext import ndb

class Ingredient(ndb.Model):
    name = ndb.StringProperty(required=True)
    image = ndb.StringProperty()

    class Meta:
        app_label = 'epicshaker'
