from google.appengine.ext import ndb

class Beverage(ndb.Model):
    name = ndb.StringProperty(required=True)
    description = ndb.StringProperty()
    image = ndb.StringProperty()
    recipe = ndb.JsonProperty(required=True) # name, amount, how to shake
    tags = ndb.StringProperty(repeated=True)

    class Meta:
        app_label = 'epicshaker'
