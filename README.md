# PiczillaMVVM

A simple MVVM app to fetch images from Flickr Api and use LiveData and LRU cache without using any external library.

## App Screens
<p align="center">
    <img src="/media/screen1.jpg" width="250" height="380">
    <img src="/media/screen2.jpg" width="250" height="380">
</p>

## App features
* App shows random images from Flickr Api.
* App has single full screen imageView to show images.
* App has two buttons (next & previous) to navigate through images.
* App uses cache strategy which means if an image has been fetched from url then it will be stored and served from cache.
* App follows MVVM (Model View ViewModel) pattern.
* No external library used for networking or image caching.
* HttpUrlConnection with AsyncTask used for api calling and LRU cache used for effective loading and caching.
* App supports landscape and portrait modes.

## Api used
Below Flickr api is used to fetch images urls:
https://api.flickr.com/services/rest/?method=<photos_method>&api_key=<api_key>&format=json&nojsoncallback=1&safe_search=1&tags=kitten&per_page=10&page=1

where,  photos_method = flickr.photos.search
        api_key = 3e7cc266ae2b0e0d78e279ce8e361736
Please register at https://www.flickr.com/services/api/ in case the given api key does not work.

Build image url from response object (farm, server, id, secret are present in response object)

https://farm${this.farm}.staticflickr.com/${this.server}/${this.id}_${this.secret}_q.jpg

## App structure
App follows below structure:
* <b>model</b>
    * Package contains POJO class for photo from api response.
* <b>viewmodel</b>
    * <b>ImagesViewModel: </b> ViewModel containing logic to fetch image urls and check cache if any bitmap is available else download bitmap from url.
* <b>repository</b>
    * Package contains class for fetching data repository of images list(JSON) and image i.e asyncTasks for fetching image urls and bitmaps.
    * <b>DownloadImageTask: </b> AsyncTask for downloading bitmap from given url. Also saves downloaded bitmap into cache.
    * <b>FetchImageUrlsTask: </b> AsyncTask for fetching images urls from Flickr api in chunks of 10 images per hit. Also parses json response.
* <b>utils</b>
    * Package contains utility class and generic cache class.
    * <b>ImagesCache: </b> Common LRU cache defined class. Any changes related to cache can be done here.
    * <b>Utility: </b> Utility class where HttpUrlConnection is defined for fetch images and download bitmaps.
* <b>view</b>
    * Package contains UI classes which needs to be shown on app.
    * <b>MainActivity: </b> Single activity which contains a fragment.
    * <b>MainFragment: </b> Fragment class which contains ImageView and two buttons which will be shown to the user.
