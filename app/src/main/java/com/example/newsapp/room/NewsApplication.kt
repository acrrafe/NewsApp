package com.example.newsapp.room

import android.app.Application

/**
 * We Need to Create this to use the application Context inside of the parameter
 * of AndroidViewModel that will be inherited inn NewsViewModel instead of directly using
 * the context of the activity or fragment because it is a bad practice.
 **/
class NewsApplication (): Application ()