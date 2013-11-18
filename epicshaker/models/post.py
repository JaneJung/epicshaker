from google.appengine.ext import ndb

class Post(ndb.Model):
    beverage_id = ndb.StringProperty(required=True)
    comment = ndb.StringProperty(required=True)

    class Meta:
        app_label = 'epicshaker'
