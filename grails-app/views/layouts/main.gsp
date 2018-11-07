<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.11.1.cupertino.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'pro_dropdown_menu.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.multiselect.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.countdown.css')}" type="text/css">

    <script src="${resource(dir: 'js', file: 'jquery.ctm.js')}" type="text/javascript"></script>
    <script src="${resource(dir: 'js', file: 'jquery-1.11.1.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'jquery-ui-1.11.1.cupertino.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js/tinymce', file: 'tinymce.min.js')}" type="text/javascript"></script>

        <link rel="stylesheet" href="${resource(dir: 'css', file: 'header.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'home.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'comments.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'page.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'fields.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'buttons.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'disabled_buttons.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'footer.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.multiselect.filter.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'handsontable.full.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'progress.css')}" type="text/css">

        <r:require modules="jquery, multiselect, slider, datepicker, tagcloud, countdown, handsontable"/>
		<g:layoutHead/>
		<g:javascript library="application"/>		
		<r:layoutResources />
	</head>

    <body>
        <g:render template="/layouts/header"/>
        <g:render template="/layouts/menu"/>
        <g:render template="/layouts/viewer"/>
        <g:layoutBody/>
        <g:render template="/layouts/footer"/>
		<div id="spinner" class="spinner" style="display:none;">
            <g:message code="spinner.alt" default="Loading&hellip;"/>
        </div>
		<r:layoutResources />
	</body>

</html>
