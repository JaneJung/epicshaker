from google.appengine.ext import ndb

class Beverage(ndb.Model):
    name = ndb.StringProperty()
    recipe = ndb.JsonProperty() # name, amount, how to shake
    tags = ndb.StringProperty(repeated=True)

    class Meta:
        app_label = 'epicshaker'
