modules = {
    application {
        resource url:'js/application.js'
    }

    multiselect {
        resource url:'js/jquery-ui-1.11.1.cupertino.js'
        resource url:'js/jquery.multiselect.js'
        resource url:'js/jquery.multiselect.filter.js'
    }

    handsontable {
        resource url:'js/jquery-1.11.1.js'
        resource url:'js/jquery-ui-1.11.1.cupertino.js'
        resource url:'js/handsontable.full.js'
    }

    tagcloud {
        dependsOn 'jquery'
        resource url:'js/jquery.tagcloud.js'
    }

    alerts {
        dependsOn 'jquery'
        resource url:'js/jquery.alerts.js'
    }

    countdown {
        dependsOn 'jquery'
        resource url:'js/jquery.plugin.js'
        resource url:'js/jquery.countdown.js'
    }

    slider {
        dependsOn 'jquery'
        resource url:'js/jquery-ui-1.11.1.cupertino.js'
        resource url:'js/jquery.ui.slider.js'
    }

    datepicker {
        dependsOn 'jquery'
        resource url:'js/datepicker.js'
    }

    animate {
        dependsOn 'jquery'
        resource url:'js/animatecollapse.js'
    }

    listbox_reorder {
        resource url:'js/listbox_reorder.js'
    }

    full_calendar {
        resource url:'js/moment.min.js'
        resource url:'js/fullcalendar.js'
    }

    raphael {
        resource url:'js/raphael.js'
        resource url:'js/g.raphael.js'
        resource url:'js/g.pie.js'
    }

    vmap {
        resource url:'js/jquery.vmap.js'
        resource url:'js/jquery.vmap.world.js'
        resource url:'js/jquery.vmap.usa.js'
    }

    spreadsheet {
        resource url:'js/jquery_spreadsheet.js'
    }

    export {
        resource '/css/export.css'
    }

//    masterStyles {
//        if (isDevMode()) {
//            resource url: 'less/style.less', attrs: [rel: 'stylesheet/less', type: 'css']
//        }
//        else {
//            resource url: 'css/style.css'
//        }
//    }
//
//
//    master {
//        dependsOn 'jquery', 'masterStyles', 'fontAwesome'
//        resource 'js/bootstrap/bootstrap.js'
//        resource url: 'js/modernizr-2.5.3-respond-1.1.0.min.js', disposition: 'head'
//        resource 'js/script.js'
//    }
//
//    learn {
//        dependsOn 'master'
//    }

}