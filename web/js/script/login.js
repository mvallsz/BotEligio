$('#btnEntrar').click(function () {
    var username = $('#username').val().trim();
    var password = $('#password').val().trim();
    if (username === "") {
        alert('Atención! \nDebe ingresar usuario');
        return false;
    } else if (password === "") {
        alert('Atención! \nDebe ingresar contraseña');
        return false;
    } else {
        var URL = "HBES/cmd";
        if (username.trim().length > 0 && password.trim().length > 0) {
//            var info = detectarInfo();
            $.ajax({
                url: 'Svl_Usuario',
                dataType: 'json',
                type: 'POST',
                data: {
                    accion: 'login',
                    username: username,
                    password: password
//                    SO: info[0],
//                    BROW: info[1],
//                    VER: info[2],
//                    CITY: info[3],
//                    COUNTRY: info[4],
//                    IP: info[5],
//                    ORG: info[6],
//                    URL: URL
                },
                success: function (data, textStatus, jqXHR) {
                    if (data.estado == 200) {
                            go('cmd', [{id: 'code', val: 'home'}], false, 'cmd');
                    } else {
                        alert('Usuario o Contraseña mal Ingresada');
                    }
                }
            });
        }
    }
    return false;
});


function detectarInfo() {
    var ua = navigator.userAgent.toLowerCase();
    var check = function (r) {
        return r.test(ua);
    };
    var DOC = document;
    var isStrict = DOC.compatMode == "CSS1Compat";
    var isOpera = check(/opera/);
    var isChrome = check(/chrome/);
    var isWebKit = check(/webkit/);
    var isSafari = !isChrome && check(/safari/);
    var isSafari2 = isSafari && check(/applewebkit\/4/); // unique to
    // Safari 2
    var isSafari3 = isSafari && check(/version\/3/);
    var isSafari4 = isSafari && check(/version\/4/);
    var isIE = !isOpera && check(/msie/);
    var isIE7 = isIE && check(/msie 7/);
    var isIE8 = isIE && check(/msie 8/);
    var isIE6 = isIE && !isIE7 && !isIE8;
    var isGecko = !isWebKit && check(/gecko/);
    var isGecko2 = isGecko && check(/rv:1\.8/);
    var isGecko3 = isGecko && check(/rv:1\.9/);
    var isBorderBox = isIE && !isStrict;
    var isWindows = check(/windows|win32/);
    var isMac = check(/macintosh|mac os x/);
    var isAir = check(/adobeair/);
    var isLinux = check(/linux/);
    var isSecure = /^https/i.test(window.location.protocol);
    var isIE7InIE8 = isIE7 && DOC.documentMode == 7;

    var jsType = '', browserType = '', browserVersion = '', osName = '';
    var ua = navigator.userAgent.toLowerCase();
    var check = function (r) {
        return r.test(ua);
    };

    if (isWindows) {
        osName = 'Windows';

        if (check(/windows nt/)) {
            var start = ua.indexOf('windows nt');
            var end = ua.indexOf(';', start);
            osName = ua.substring(start, end);
        }
    } else {
        osName = isMac ? 'Mac' : isLinux ? 'Linux' : 'Other';
    }

    if (isIE) {
        browserType = 'IE';
        jsType = 'IE';

        var versionStart = ua.indexOf('msie') + 5;
        var versionEnd = ua.indexOf(';', versionStart);
        browserVersion = ua.substring(versionStart, versionEnd);

        jsType = isIE6 ? 'IE6' : isIE7 ? 'IE7' : isIE8 ? 'IE8' : 'IE';
    } else if (isGecko) {
        var isFF = check(/firefox/);
        browserType = isFF ? 'Firefox' : 'Others';
        ;
        jsType = isGecko2 ? 'Gecko2' : isGecko3 ? 'Gecko3' : 'Gecko';

        if (isFF) {
            var versionStart = ua.indexOf('firefox') + 8;
            var versionEnd = ua.indexOf(' ', versionStart);
            if (versionEnd == -1) {
                versionEnd = ua.length;
            }
            browserVersion = ua.substring(versionStart, versionEnd);
        }
    } else if (isChrome) {
        browserType = 'Chrome';
        jsType = isWebKit ? 'Web Kit' : 'Other';

        var versionStart = ua.indexOf('chrome') + 7;
        var versionEnd = ua.indexOf(' ', versionStart);
        browserVersion = ua.substring(versionStart, versionEnd);
    } else {
        browserType = isOpera ? 'Opera' : isSafari ? 'Safari' : '';
    }

    //SO BROW VER CITY COUNTRY IP ORG

    var info = [osName, browserType, browserVersion, window.city, window.country, window.ip, window.org];

    return info;
}

function getParameters(url) {

    // get query string from url (optional) or window
    var queryString = url ? url.split('?')[1] : window.location.search.slice(1);

    // we'll store the parameters here
    var obj = {};

    // if query string exists
    if (queryString) {

        // stuff after # is not part of query string, so get rid of it
        queryString = queryString.split('#')[0];

        // split our query string into its component parts
        var arr = queryString.split('&');

        for (var i = 0; i < arr.length; i++) {
            // separate the keys and the values
            var a = arr[i].split('=');

            // in case params look like: list[]=thing1&list[]=thing2
            var paramNum = undefined;
            var paramName = a[0].replace(/\[\d*\]/, function (v) {
                paramNum = v.slice(1, -1);
                return '';
            });

            // set parameter value (use 'true' if empty)
            var paramValue = typeof (a[1]) === 'undefined' ? true : a[1];

            // (optional) keep case consistent

            // if parameter name already exists
            if (obj[paramName]) {
                // convert value to array (if still string)
                if (typeof obj[paramName] === 'string') {
                    obj[paramName] = [obj[paramName]];
                }
                // if no array index number specified...
                if (typeof paramNum === 'undefined') {
                    // put the value on the end of the array
                    obj[paramName].push(paramValue);
                }
                // if array index number specified...
                else {
                    // put the value at that index number
                    obj[paramName][paramNum] = paramValue;
                }
            }
            // if param name doesn't exist yet, set it
            else {
                obj[paramName] = paramValue;
            }
        }
    }

    return obj;
}