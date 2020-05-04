/**
 * 
 */

function changeSwitch(input) {
    if (input.checked)
        document.getElementById('generalSession').disabled = false;
    else
        document.getElementById('generalSession').disabled = true;
}

function isValidDate(date) {
    let fecha = date;

    // array with all the posible holidays of the year excluding easter
    let holiday = [
        '01-01', // New year
        '04-11', // Rivas battle
        '05-01', // Labor day
        '07-25', // Annexation of Guanacaste
        '08-02', // Our Lady of Los Ángeles
        '08-15', // Mother's day
        '09-15', // Independance's day
        '10-12', // Day of the Cultures
        '12-25'  // Christmas Day
    ];

    for (let i = 0; i < holiday.length; i++) {
        let aux = '' + fecha.getFullYear + '-' + holiday[i];
        if (date === aux)
            return false;
    }

    if (fecha.getMonth() === 2 || fecha.getMonth() === 3) {
        let URL = "https://www.googleapis.com/calendar/v3/calendars/" +
                "es.cr%23holiday%40group.v.calendar.google.com/events?key=AIzaSyAJuTl"
                + "-gxVHcY80RvGL8T9fzY8sgfwU8Xw";
        var result = true;
        $.getJSON(URL).then(data => {
            data.items.forEach(item => {
                // if the day is eastern
                if (item.summary === 'Jueves Santo' || item.summary === 'Viernes Santo') {
                    if (date === item.start.date) {
                        result = false;
                        return false;
                    }

                }
            })

            return result;
        });

    }
    return true;
}


function validationDeadLine(){
	let confirm = document.getElementById('deadline').value;
	if(confirm===''){
		 alert('Por Favor Confirmar Fecha de Vencimiento!!');
	      return false;
	}
}



function changeComboType() {
	let combo = document.getElementById('comboTypes').value;
	let labelName = document.getElementById('labelName');
	let labelEmail = document.getElementById('labelEmail');
	let textName = document.getElementById('username');
	let textEmail = document.getElementById('email');
	let buttonAdd = document.getElementById('btnAdd');
	let buttonSubs = document.getElementById('btnSubs');
	let line = document.getElementById('line');
	if (parseInt(combo,10) !== 1) {
		textName.value = '';
		textEmail.value = '';
		textName.type = 'text';
		textEmail.type = 'email';
		labelName.style.visibility = 'visible';
		labelEmail.style.visibility = 'visible';
		buttonAdd.style.visibility = 'visible';
		buttonSubs.style.visibility= 'visible';
		line.style.visibility= 'visible';
	} else {
		textName.value = '';
		textEmail.value = '';
		textName.type = 'hidden';
		textEmail.type = 'hidden';
		labelName.style.visibility = 'hidden';
		labelEmail.style.visibility = 'hidden';
		buttonAdd.style.visibility = 'hidden';
		buttonSubs.style.visibility= 'hidden';
		line.style.visibility= 'hidden';
		const myNode = document.getElementById('responsable');
		while (myNode.lastChild.nodeName!='#text') {
			if(myNode.lastChild.nodeName!='#text'){
				myNode.removeChild(myNode.lastChild);
			}
		}
	}
}

//various responsables for each accord
function accord_Responsables(){
	var elmnt = document.getElementById('responsable');
	var name = document.getElementById('nameR');
	var mail = document.getElementById('emailR');
	var nameI = name.children[1];
	var mailI = document.getElementById('email');
	var line = document.getElementById('line');
	var cln = name.cloneNode(true);
	cln.children[1].value='';
	var cln1 = mail.cloneNode(true);
	cln1.children[1].value='';
	var cln2 = line.cloneNode(true);
	elmnt.appendChild(cln);
	elmnt.appendChild(cln1);
	elmnt.appendChild(cln2);
}

//remove the last reponsable from the adds
function accord_Delete_Last_Responsables(){
	const myNode = document.getElementById('responsable');
	if(myNode.lastChild.nodeName!='#text'){
		for(var i=0; i<3; i++){
			myNode.removeChild(myNode.lastChild);
		}
	}
}



function getDeadline_NaturalDays(_date) {
    let days = parseInt(document.getElementById('days').value, 10);
    // adding the days to the date
    let date = addDays(days, _date);

    // if the resulting date is a weekend
    if (date.getDay() === 6 || date.getDay() === 0) {
        date = fixWeekend(date);
    }

    // if the resulting date is not a holiday
    // then return it
    // else just add one day to the resulting date
    // until isnt a holiday or a weekend
    if (isValidDate(date)) {
        return date;
    } else {

        for (; ; ) {
            date = addDays(1, date);
            if (date.getDay() === 6 || date.getDay() === 0) {
                date = fixWeekend(date);
            }
            if (isValidDate(date))
                return date;
        }

    }

}




// this function gets the deadline without counting the weekends
// or the holidays
function getDeadline_BussinessDays(date) {
 let days = parseInt(document.getElementById('days').value, 10);
 // counting the current day
 let aux = 1;

 for (; ; ) {
     // if the counting is equal to the amount of days selected
     if (aux === days) {
         // if isnt a holiday or a weekend return the current date
         if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date)) {
             return date;
         } else {

             // if the resulting date is a weekend, fix it
             if (date.getDay() === 0 || date.getDay() === 6) {
                 date = fixWeekend(date);
             } else {
                 // add one day to the resulting date to avoid the holiday
                 date = addDays(1, date);
             }
             for (; ; ) {
                 // return the resulting date if isnt a weekend or a holiday
                 if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date))
                     return date;
                 else
                     date = addDays(1, date);
                 		
                 
                 // else just keep adding one day to the resulting date until
					// isnt a
                 // holiday or a weekend
             }
         }
     }

     // it only counts when isnt weekend or holiday
     if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date))
         aux++;

     // adds one day to the date
     date = addDays(1, date);
 }


}

// this function adds 1 day to the date if the day is sunday
// and 2 days if the day is saturday
function fixWeekend(date) {
  if (date.getDay() === 6)
      return addDays(2, date);
  else
      return addDays(1, date);


}


function formatDate(d) {
    var month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [day, month, year].join('-');
}




function setDeadline() {
    date = new Date();
    let output = document.getElementById('deadline');
    let select = document.getElementById('comboDays').value;
    if (document.getElementById('days').value !== '' && document.getElementById('days').value != 0) {
    	let days = parseInt(document.getElementById("days").value, 10);
    	 if (days > 60)
    		 if (!confirm("Usted ha seleccionado un plazo de: " + days + " días.\n ¿Desea Continuar?"))
    			 { 
    			 	document.getElementById('days').value = '';
    			 	output.value='';
    			 	return;
    			 
    			 } 
    	 
             
          let newDate = select === '1' ? getDeadline_BussinessDays(date) : getDeadline_NaturalDays(date);
          let format = formatDate(newDate);
            
    	  output.value = format;
    } 
    else {
    	
    	bootbox.alert('Por favor ingrese un plazo o que sea mayor a 0(cero)');
        
    }
}


function addDays(days, date) {

    var result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}


function changeSwitch(input) {
	
    document.getElementById('sessionDate').value='';
	
    if (input.checked){
        document.getElementById('sessionDate').disabled = false;
        document.getElementById('sessionDate').required = true;
    }
    
    else{
        document.getElementById('sessionDate').disabled = true;
        document.getElementById('sessionDate').required = false;
        
    }
}

async function submitForm(){
	let json = await toJson();
	document.getElementById('responsables').value=json;
	$("#form1").submit();
}


async function toJson(){
	let usernames=document.getElementsByName('usernameResponsable');
	let emails=document.getElementsByName('emailResponsable');
	
	if(usernames.length != emails.length)
		return '';
	
	let arrObj=[];
	
	for(let i=0; i<usernames.length; i++ ){
		
		if(usernames[i].value ===''|| emails[i].value==='')
			continue;
		
		let obj={};
		obj.username=usernames[i].value;
		obj.email=emails[i].value;
		
		arrObj.push(obj);
	}
	
	return JSON.stringify(arrObj);
}
