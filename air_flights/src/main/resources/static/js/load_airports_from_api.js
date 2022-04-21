function load_airports() {
    var airport_code = $('#input1').find(":selected").val();
    var cont = document.getElementById("continue")
    var secSel = document.getElementById("input2")
    cont.disabled = false;
    secSel.disabled = false;
    var url = 'http://localhost:8080/api/book/'+airport_code;
    $.getJSON(url, function(data) {
        var str = ""
        for (var airport of data) {
            str += '<option value="'+airport.airportCode+'">'+airport.country+', '+airport.city+' ('+airport.airportCode+')</option>'
        }
        secSel.innerHTML = str
    });
}

function load_connections() {
    var airport_code = $('#sel').find(":selected").val();
    var list_al = document.getElementById("list_already_connected")
    var list_non = document.getElementById("list_non_connected")
    var url = 'http://localhost:8080/api/make_connections/'+airport_code;
    var url1 = 'http://localhost:8080/api/book/'+airport_code;

    var list = $.getJSON(url, function(data) {
        console.log(data)
        if (data.length !== 0) {
            var str = ""
            for (var airport of data) {
                str += '<div class="form-check"><input name="connections" class="form-check-input" type="checkbox" value="' + airport.airportCode + '"><label class="form-check-label">' + airport.country + ', ' + airport.city + ' (' + airport.airportCode + ')</label></div>'
            }
            str+= '<input type="hidden" value="000" name="connections"><input type="hidden" value="'+ airport_code + '" name="selected_airport"><input type="hidden" value="mk_con" name="action"><input type="submit" class="btn btn-primary my-1" value="Make connections">'
            list_non.innerHTML = str
        } else {
            list_non.innerHTML = '<h5>This airport have already connections with all airports</h5>'
        }
    });


    $.getJSON(url1, function (data) {
        console.log(data)
        if (data.length !== 0) {
            var str = ""
            for (var airport of data) {
                str += '<li class="list-group-item d-flex justify-content-between align-items-center">' + airport.country + ', ' + airport.city + ' (' + airport.airportCode + ')<button type="submit" name="del_airport" value="'+ airport.airportCode +'" class="badge badge-primary badge-pill">Delete</button></li>'
            }
            str += '<input type="hidden" value="'+ airport_code + '" name="sel_airport">'
            list_al.innerHTML = str
        } else {
            list_al.innerHTML = '<h5>This airport have no connections yet</h5>'
        }
    });
}













  


  