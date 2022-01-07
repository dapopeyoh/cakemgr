# Cake App
This is a simple cake repository

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Docker (including docker-compose)

### Running the Container
1. Clone the repo
```sh
$ git clone https://github.com/dapopeyoh/cakemgr.git
```
2. cd into directory
```sh
$ cd cakemgr
```
3. run docker compose
```sh
$ docker-compose up -d
```
The app should now run on port 8080


## REST API
The REST API to the cake repository is described below.

### Get All Cakes

URL: `/api/v1/cakes`

Method: `GET`

URL Params:
```sh
Optional:
    page=[integer]
    size=[integer]
    sort=[string]
```

Sample Response:
```sh
{
    "message": "Request Successful",
    "data": [
        {
            "id": 1,
            "title": "Lemon cheesecake",
            "description": "A cheesecake made of lemon",
            "image": "https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"
        },
        {
            "id": 2,
            "title": "victoria sponge",
            "description": "sponge with jam",
            "image": "http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg"
        },
        {
            "id": 3,
            "title": "Carrot cake",
            "description": "Bugs bunnys favourite",
            "image": "http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg"
        },
        {
            "id": 4,
            "title": "Banana cake",
            "description": "Donkey kongs favourite",
            "image": "http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg"
        },
        {
            "id": 5,
            "title": "Birthday cake",
            "description": "a yearly treat",
            "image": "http://cornandco.com/wp-content/uploads/2014/05/birthday-cake-popcorn.jpg"
        }
    ],
    "pagination": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 5
    }
}
```
___

### Get Single Cake

URL: `/api/v1/cakes/{id}`

Method: `GET`

Sample Success Response:
```sh
{
    "message": "Request Successful",
    "data": {
        "id": 3,
        "title": "Carrot cake",
        "description": "Bugs bunnys favourite",
        "image": "http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg"
    }
}
```

Sample Error Response:
```sh
{
    "message": "Cake with id: 33 not found"
}
```
---

### Add New Cake

URL: `/api/v1/cakes`

Method: `POST`

Sample Payload: `{
"title": "Sponge",
"description": "Soft and fluffy",
"image": "http://images.com/image.jpg"
}`

Sample Success Response:
```sh
{
    "message": "Request Successful",
    "data": {
        "id": 7,
        "title": "Sponge",
        "description": "Soft and fluffy",
        "image": "http://images.com/image.jpg"
    }
}
```

Sample Error Response:
```sh
{
    "message": "Cake with title: Sponge already exists"
}
```
