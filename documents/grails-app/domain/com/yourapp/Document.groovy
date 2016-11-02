package com.yourapp


import grails.rest.*

@Resource(readOnly = false, formats = ['json', 'xml'])
class Document {

    String title
    int pages

}