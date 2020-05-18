const unitInput = document.getElementById("unitPicker");
const forecastTable = document.getElementById("forecast");
const carousel = document.getElementById("carousel");
const slides = [document.getElementById("slide1"), document.getElementById("slide2"), document.getElementById("slide3")];
const favorites = document.getElementById("favCities");

var currentFavorite;
populateFavorites();

/* to handle the unit change  */
var slider = document.getElementById('unitPicker');
slider.addEventListener('change', function() {
	histData = JSON.parse(document.getElementById("histData").innerHTML);
//	drawHistChart(histData);
    if(document.getElementById("currCity").innerHTML != ""){
        var unit = unitInput.value == "on" ? "F" : "C";
        if(unit == "F"){
            unit = "C";
            unitInput.value = "off";
            var currTemp = document.getElementById("weather").innerHTML;
            currTemp = currTemp.split(" ");
            var newTemp = Math.round((((currTemp[0] - 32)*5)/9));
            document.getElementById("weather").innerHTML = newTemp + " °C";
            /* update the table  */
            var table = document.getElementById("forecast");
            for (var i = 2; i < 4; i++) {
                for (var j = 0; j < 5; j++){
                    currTemp = table.rows[i].cells[j].innerHTML;
                    currTemp = currTemp.split(" ");
                    newTemp = Math.round((((currTemp[0] - 32)*5)/9));
                    table.rows[i].cells[j].innerHTML = newTemp + " °C";
                }
            }
        }
        else if(unit == "C"){
            unit = "F";
            unitInput.value = "on";
            var currTemp = document.getElementById("weather").innerHTML;
            currTemp = currTemp.split(" ");
            var newTemp = Math.round(((currTemp[0] * 9/5)+32));
            document.getElementById("weather").innerHTML = newTemp + " °F";
            /* update the table  */
            var table = document.getElementById("forecast");
            for (var i = 2; i < 4; i++) {
                for (var j = 0; j < 5; j++){
                    currTemp = table.rows[i].cells[j].innerHTML;
                    currTemp = currTemp.split(" ");
                    newTemp = Math.round(((currTemp[0] * 9/5)+32));
                    table.rows[i].cells[j].innerHTML = newTemp + " °F";
                }
            }
        }
    }
});

function favButton(element, loc){
    this.element = element;
    this.loc = loc;
}

function populateFavorites(){
    var requestUrl = "/favorites";
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = JSON.parse(this.responseText);
            console.log(response);
            for(item of response){
                addFavorite(item);
            }
        }
        else if(this.status == 400){
            setMessage("No locations found.")
        }
    };
    
    xhttp.open("GET", requestUrl, true);
    xhttp.send();
}

function removeFavorite(loc){
    var requestUrl = "/favorites";
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var myTable = document.getElementById("favCities");
            var rowCount = myTable.rows.length;
            for (var x=rowCount-1; x>0; x--) {
               myTable.deleteRow(x);
            }
            populateFavorites();
        }
        else if(this.status == 400){
            setMessage("No locations found.")
        }
    };
    xhttp.open("DELETE", requestUrl, true);
    xhttp.setRequestHeader("Content-type", "text/plain");
    xhttp.send(document.getElementById("currCity").innerHTML);
}

function addFavorite(loc){
    var row = favorites.insertRow(-1);
    var cell = row.insertCell(0);
    cell.innerHTML = loc;
    cell.style.borderStyle = "solid";
    cell.style.background = "#007bff";
    cell.style.color = "white";
    cell.style.borderRadius = "10px";
    cell.style.textAlign = "center";
    
         cell.onclick = function(){
        selectFavorite(loc);
    };		
}

function selectFavorite(loc){
    var unit = unitInput.value == "on" ? "Fahrenheit" : "Celsius";
    document.getElementById("currCity").innerHTML = loc;
    var requestUrl = "/detailedweather?loc=" + loc + 
            "&unit=" + unit;
    
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = JSON.parse(this.responseText);
            fillForecast(response.dailyForecasts);
            fillCarousel(response.placeImages);
            displayCurrent(response.weatherLocation);
            drawHistChart(response.monthTemps);
            console.log(response.monthTemps);
            document.getElementById("histData").innerHTML = JSON.stringify(response.monthTemps);
        }
        else if(this.status == 400){
            setMessage("No locations found.")
        }
    };
    
    xhttp.open("GET", requestUrl, true);
    xhttp.send();
}

function clearTable(){
    for(var i = forecastTable.rows.length - 1; i >= 0; i -= 1){
        forecastTable.deleteRow(i);
    }
}

function fillForecast(forecast){
    clearTable();
    
    var icons = forecastTable.insertRow(-1);
    var dates = forecastTable.insertRow(-1);
    var highs = forecastTable.insertRow(-1);
    var lows = forecastTable.insertRow(-1);
    
    for(const day of forecast){
        var icon = document.createElement("img");
        console.log(day.weatherIcon);
        icon.src = day.weatherIcon;
        icon.style.maxHeight = "90%";
        icon.style.width = "100%";
        icons.insertCell(-1).appendChild(icon);
        dates.insertCell(-1).innerHTML = day.date.month + ", " + day.date.day;
        highs.insertCell(-1).innerHTML = day.high + " °" + day.tempUnit[0];
        lows.insertCell(-1).innerHTML = day.low + " °" + day.tempUnit[0];
    }
}

function fillCarousel(images){
    for(var i = 0; i < images.length; i++){
        var idName = "img" + i.toString();
        var img = document.createElement("img");
        img.src = images[i];
        slides[i].innerHTML = "";
        slides[i].appendChild(img);
        img.setAttribute("id", idName);
        document.getElementById(idName).style.maxHeight = "200px";
        document.getElementById(idName).style.position = "fixed";
        document.getElementById(idName).style.border = "4px solid #d3d3d3";
    }
}

function displayCurrent(weather){
    var locationName = weather.city + ", " + weather.country;
    var temp = weather.currentTemp;
    var desc = weather.weather;
    var unit = unitInput.value == "on" ? "F" : "C";
    
    document.getElementById("date").innerHTML = (new Date()).toLocaleDateString();
    document.getElementById("location").innerHTML = locationName;
    document.getElementById("weather").innerHTML = temp + " °" + unit;
    document.getElementById("description").innerHTML = desc;
    var img = document.getElementById("weatherIcon");
    img.src = weather.weatherGraphicStatic;
    img.style.width = "600px";
    document.getElementById("currentWrapper").style.padding = "10px";
    /* img.style.height = "100px"; */
    /* img.onmouseover = setSrc(img, weather.weatherGraphicAnimate);
    img.onmouseout = setSrc(img, weather.weatherGraphicStatic); */
}

function setSrc(img, src){
    return function(){
        img.src = src;
    };
}

function drawHistChart(histData) {
    var entries = [['Months', 'Low Temperature', 'High Temperature']];
    for(month of histData){
        /* ensure that we have the correct units in the graph  */
        var monthLow = month.avgLow;
        var monthHigh = month.avgHigh;
        console.log(month);
        
        if(document.getElementById("currCity").innerHTML != ""){
            var unit = unitInput.value == "on" ? "F" : "C";

            if(unit == "F"){
                monthLow = Math.round((((monthLow - 32)*5)/9));
            }
            else if(unit == "C"){
                monthHigh = Math.round(((monthHigh * 9/5)+32));
            }	
        
        entries.push([month.month, monthLow, monthHigh]);
    }
    
    var data = google.visualization.arrayToDataTable(entries);

    var options = {
      title: 'Average Highs And Lows Of Previous Year',
      curveType: 'function',
      legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
    document.getElementById('curve_chart').style.border = "4px solid #E0E0E0";

    chart.draw(data, options);
  }
}

var slideIndex = 0;
showSlides();

function showSlides() {
  var i;
  var slides = document.getElementsByClassName("slide");
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  slideIndex++;
  if (slideIndex > slides.length) {slideIndex = 1}   
  slides[slideIndex-1].style.display = "block";

  setTimeout(showSlides, 3000); // Change image every 3 seconds
}