recipes
=======

A place for recipes of all types and uses for the CIC

Cloudify Recipes for CIC-related projects.

Folder structure:

    services: Individual services recipes (e.g. tomcat, apacheWeb, mysql) for services that will likely be reusable 
    by multiple applications.
    
    apps: Self-contained collections of recipes which may include copies of recipes from the services folder. 
    (Since PaaS application recipes must be fully self-contained under the application recipe folder, 
    you may need to copy a service recipe under that folder.)
