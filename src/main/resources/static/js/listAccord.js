/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {

    initTable();
    //subsButton();
});

function initTable() {
    $('#tableAcc').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ Acuerdos",
            "zeroRecords": "No se encontraron resultados",
            "info": "Mostrando Acuerdos del _START_ al _END_ de un total de _TOTAL_ registros",
            "infoEmpty": "Mostrando Acuerdos del 0 al 0 de un total de 0 registros",
            "infoFiltered": "(filtrado de un total de _MAX_ Acuerdos)",
            "sSearch": "Filtrar:",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "Último",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            },
            "sProcessing": "Procesando..."
        },
        "lengthChange": false,
        "iDisplayLength": 5,
        "destroy": true
        
    });
}



function changeSelect(value) {
    let field = document.getElementById('searchText');
    let button = document.getElementById('seacrhButton');
    let combo = document.getElementById('selectType');
    console.log(field);
    if (value === 'sessionDate' || value === 'incorDate') {
        field.type = 'text';
        $("#searchText").datepicker({dateFormat: 'dd-mm-yy'});
        field.value = "";
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
        combo.value = 'A';

    } else
    if (value === 'accNumber') {
        field.value = "";
        field.type = 'text';
        $("#searchText").datepicker( "destroy" );
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
    } else if (value === 'sessionType') {
        field.value = "";
        field.type = 'hidden';
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'visible';
    } else if (value === 'allAccords') {
        field.value = "";
        field.type = 'hidden';
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
    } else if (value === 'expiredAccords') {
        field.value = "";
        field.type = 'hidden';
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
    } else {
        field.value = "";
        combo.value = 'A';
        field.type = 'hidden';
        button.style.visibility = 'hidden';
        combo.style.visibility = 'hidden';
    }



}

function searchAccord() {
	let titleDiv=document.getElementById('titleh3');
    let parameter = document.getElementById('serchType').value;
    if (parameter !== 'notSelected') {
        switch (parameter) {
            case 'sessionDate':
            	titleDiv.innerHTML='Acuerdos Por Fecha de Sesión'
                SearchBySessionDate();
                break;

            case 'incorDate':
            	titleDiv.innerHTML='Acuerdos Por Fecha de Incorporación'
                searchByIncorDate();
                break;


            case 'sessionType':
            	titleDiv.innerHTML='Acuerdos Por Tipo de Acuerdo';
                searchBySessionType();
                break;

            case 'accNumber':
            	titleDiv.innerHTML='Acuerdos Por Número de Acuerdo';
                searchByAccNumber();
                break;

            case 'allAccords':
            	titleDiv.innerHTML='Todos los Acuerdos';
                searchAllAccords();
                break;

            case 'expiredAccords':
            	titleDiv.innerHTML='Acuerdos Vencidos';
                searchExpiredAccords();
                break;
        }
    }
}

function SearchBySessionDate() {
    let sessiondate = document.getElementById('searchText').value;
    let _url = "/api/accords/sessionDate/" + sessiondate;
    console.log(_url);

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(accords => {
            	console.log(accords);
                $('#tableAcc').DataTable().clear().destroy();
                var parent = $("#accordList");
                parent.html("");
                accords.forEach(item => {
                    list(parent, item);
                });

            })
            .then(() => {
                initTable();
            })
            .catch(error => {
                console.log(error);
            });
}

var accCounter=0;
function list(parent, accord) {
    let now = new Date();
    console.log(now);
    let date = new Date(accord.deadline);

    var tr = $("<tr/>");
    tr.html(
            "<td>" + accord.accNumber + "</td>"
            + "<td>" + formatDate(accord.incorporatedDate) + "</td>"
            + "<td>" + formatDate(accord.sessionDate) + "</td>"
            + "<td>" + formatDate(accord.deadline) + "</td>"
            + "<td>" + accord.type.description + "</td>"
            + "<td id=\"stateTD" + accCounter + "\">" + accord.state.description + "</td>"
            + "<td> <a href=\"/accords/edit/"+ accord.accNumber+"\">" + "<button type=\"button\" style='text-align: center' class=\"btn btn-primary\">"
            + "Editar</button></a></td>"
            );
    parent.append(tr);
  
    let state = 'stateTD' + accCounter;
    if (now > date)
        tr.css('backgroundColor', 'rgb(242, 110, 110)');
    else
    if (parseInt(accord.state.id, 10) === 0)
        document.getElementById(state).style.backgroundColor = '#00D781';

    else
    if (parseInt(accord.state.id, 10) === 1)
        document.getElementById(state).style.color = 'red';

    else if (parseInt(accord.state.id, 10) === 2)
        document.getElementById(state).style.backgroundColor = '#FFE57A';




    accCounter++;

}

function searchByIncorDate() {

    let incordate = document.getElementById('searchText').value;
    let _url = "/api/accords/incorDate/" + incordate;

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(accords => {
                $('#tableAcc').DataTable().clear().destroy();
                var parent = $("#accordList");
                parent.html("");
                accords.forEach(item => {
                    list(parent, item);
                });

            }).then(() => {
        initTable();
    })
            .catch(error => {
                console.log(error);
            });
}

function searchBySessionType() {
    let type = document.getElementById('selectType').value;
    let _url = "/api/accords/type/" + type;

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(accords => {
                $('#tableAcc').DataTable().clear().destroy();
                var parent = $("#accordList");
                parent.html("");
                accords.forEach(item => {
                    list(parent, item);
                });

            })
            .then(() => {
                initTable();
            })
            .catch(error => {
                console.log(error);
            });
}


function searchByAccNumber() {
    let accnumber = document.getElementById('searchText').value;
    let _url = "/api/accords/accNumber/" + accnumber;

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(accords => {
                console.log(accords);
                $('#tableAcc').DataTable().clear().destroy();
                var parent = $("#accordList");
                parent.html("");
                accords.forEach(item => {
                    list(parent, item);
                });

            }).then(() => {
        initTable();
    })
            .catch(error => {
                console.log(error);
            });
}


function searchAllAccords() {
    let _url = "/api/accords/get/allAccords";
    fetch(_url)
            .then(res => {
                console.log(res);
                return res.json();
            })
            .then(accords => {
                $('#tableAcc').DataTable().clear().destroy();
                var parent = $("#accordList");
                parent.html("");
                accords.forEach(item => {
                    list(parent, item);
                });
            }).then(() => {

        initTable();
    })

            .catch(error => {
                console.log(error);
            });
}








function formatDate(date) {
	let d=stringToDate(date);
    var month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [day, month, year].join('-');
}

function stringToDate(str){
	return new Date(str.split('-').join('/'));
	
}
