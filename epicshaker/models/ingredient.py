from google.appengine.ext import ndb

class Ingredient(ndb.Model):
    name = ndb.StringProperty(required=True)
    image = ndb.BlobProperty()

    class Meta:
        app_label = 'epicshaker'
