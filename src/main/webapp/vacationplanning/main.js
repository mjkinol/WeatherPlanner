

function getResults(params, callback){
    var requestUrl = "../locationsbyweather?loc=" + params.loc + 
			"&max=" + params.maxTemp +
			"&min=" + params.minTemp + 
			"&precip=" + params.precipType + 
			"&results=" + params.results + 
			"&rad=" + params.rad +
            "&unit=" + params.unit;
    
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = JSON.parse(this.responseText);
            console.log(response);
            callback(response.slice(0,5));
            setUpPagination(response, response.length);
        }
        else if(this.status == 400){
        	displayErrorMessage("No locations found.");
        }
    };
    
    xhttp.open("GET", requestUrl, true);
    xhttp.send();
}

const fieldMap = {
    "temp-low": "Temperature Low",
    "temp-high": "Temperature High",
    "location": "Location",
    "radius": "Radius"
}

function removeError(){
    for (const id in fieldMap) {
        document.getElementById(id).classList.remove("error-input");
    }

    document.getElementById("error-message").innerHTML = "";
}

function setError(field){
    document.getElementById(field).classList.add("error-input");
    displayErrorMessage("Illegal value for input " + fieldMap[field]);
}

function displayErrorMessage(message){
    document.getElementById("error-message").innerHTML = message;
}

function isValidInput(params){
    if(params.minTemp == "")
        setError("temp-low");
    else if(params.maxTemp == "" || parseInt(params.maxTemp) <  parseInt(params.minTemp))
        setError("temp-high");
    else if(params.results == "" || parseInt(params.results) < 0)
        setError("num-results");
    else if(params.loc == "" || (!isNaN(params.loc) && params.loc.length != 5))
        setError("location");
    else if(params.rad == "" || parseInt(params.rad) < 0)
        setError("radius");
    else
        return true;
    
    return false;
}

function formatLocation(loc){
	console.log(loc);
	var res = loc;
	console.log(res);
	if(res!="0"){
	res = res.replace(" ", "+");
	res = res.replace("/", "+");
	}
	return res;
}
var tableManager;


document.addEventListener("DOMContentLoaded", function(){
	tableManager = new TableManager(document.getElementById("table"));
});

function submitForm(){
    const tempLowIn = document.getElementById("temp-low");
    const tempHighIn = document.getElementById("temp-high");
    const precipTypeIn = document.getElementById("precip-type");
    const locIn = document.getElementById("location");
    const radIn = document.getElementById("radius");
    const unitIn = document.getElementById("unit");
    
    params = {
        minTemp: tempLowIn.value,
        maxTemp: tempHighIn.value,
        precipType: precipTypeIn.value,
        results: 20,
        loc: formatLocation(locIn.value),
        rad: radIn.value,

        unit: unitIn.checked ? "Fahrenheit" : "Celsius"
    }

    removeError();
    tableManager.clearContent();

    if(isValidInput(params)){
        getResults(params, displayResults)
    }

    return false;
}

function setUpPagination(results, numResults){
	document.getElementById("pag").innerHTML = '';
	var numResultsPerPage = 5;
	var numPages = Math.ceil(numResults / numResultsPerPage);
	
//	Set up the left pag arrow
	leftArrow = document.createElement("button");
	leftArrow.id = "leftPagArrow";
	leftArrow.innerHTML = "<<";
	leftArrow.style = "margin-right:10px;";
	leftArrow.onclick = function() {
		var currNum = parseInt(document.getElementById("pag").value.charAt(10));
		if (currNum > 1){
			currNum -= 1;
			document.getElementById('pag_button' + currNum.toString()).click();
			document.getElementById("pag").value = 'pag_button' + currNum.toString();
		}
	}
	document.getElementById("pag").appendChild(leftArrow);
	
//	set up the clickable numbers
	var btns = [];
	for(var page = 1; page <= numPages; page++) {
		btns[(page-1)] = document.createElement("button");
		btns[(page-1)].id = 'pag_button' + page.toString();
		btns[(page-1)].innerHTML = page;
		btns[(page-1)].style = "border:thin solid #dee2e6; height: auto; padding: 10px 20px; flex-grow: 1; margin-right:2px";
		btns[(page-1)].value = JSON.stringify(results);
		btns[(page-1)].onclick = function() {
			var res = JSON.parse(this.value);
			document.getElementById("pag").value = 'pag_button' + this.innerHTML;
			var pageResults = res.slice((parseInt(this.innerHTML)-1)*numResultsPerPage, parseInt(this.innerHTML)*numResultsPerPage);
			displayResults(pageResults);
		}
		document.getElementById("pag").appendChild(btns[(page-1)]);
	}
	
//	Set up the right pag arrow
	rightArrow = document.createElement("button");
	rightArrow.id = "rightPagArrow";
	rightArrow.innerHTML = ">>";
	rightArrow.style = "margin-left:8px;"
	rightArrow.onclick = function() {
		var currNum = parseInt(document.getElementById("pag").value.charAt(10));
		if (currNum < numPages){
			currNum += 1;
			document.getElementById('pag_button' + currNum.toString()).click();
			document.getElementById("pag").value = 'pag_button' + currNum.toString();
		}
	}
	document.getElementById("pag").appendChild(rightArrow);
	
//	initialize the value of the current pagination page
	document.getElementById("pag").value = 'pag_button1';
	console.log(document.getElementById("pag").value);
}

var res;

function displayResults(results){
	res = results;
    var rows = []
    var i=0
	for(result of results){
		rows.push(new row(result.city, result.country, result.avgMinTemp, result.avgMaxTemp, 
				result.distance, result.isFavorite, result.numLikes, i));
		i++;
    }
    
    if(rows.length == 0){
        displayErrorMessage("No locations found.");
        return;
    }
    localStorage.sort = "false";
	rows.sort((a, b) => (a.distance > b.distance) ? 1 : -1);
    
    var rawRows = rows.map((row) => [row.city, row.country, row.minTemp, row.maxTemp, row.distance, createFavButton(row.isFavorite, row.city + "," + row.country), row.numLikes, createLikeButton(row.city + "," + row.country, row.likeButton)]);

    tableManager.setHeaders(["City", "Country", "Avg Min Temp", "Avg Max Temp", "Distance", "Favorite", "Likes", ""]);
    tableManager.setRows(rawRows);
}

function createFavButton(isFav, loc){
    loc = formatLocation(loc);
    return isFav ? fav.createRemoveButton(loc) : fav.createAddButton(loc);
}

function createLikeButton(loc, num){
    loc = formatLocation(loc);
    return createAddLikeButton(loc, num);
}


class row {
    constructor(city, country, minTemp, maxTemp, distance, isFavorite, numLikes, likeButton) {
        this.city = city;
        this.country = country;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.distance = distance;
        this.isFavorite = isFavorite;
        this.numLikes = numLikes;
        this.likeButton = likeButton
    }
}

function addLike(loc, num){
    var requestUrl = "/likes"

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        	console.log("a");
            res[num].numLikes = res[num].numLikes+1;
            console.log("a");
            var rows = []
            var i=0
        	for(result of res){
        		rows.push(new row(result.city, result.country, result.avgMinTemp, result.avgMaxTemp, 
        				result.distance, result.isFavorite, result.numLikes, i));
        		i++;
            }
            
            if(rows.length == 0){
                displayErrorMessage("No locations found.");
                return;
            }
            
            sort = localStorage.getItem("sort");
        	if(sort == "true"){
        		rows.sort((a, b) => (a.numLikes < b.numLikes) ? 1 : -1);
        	}
            
            var rawRows = rows.map((row) => [row.city, row.country, row.minTemp, row.maxTemp, row.distance, createFavButton(row.isFavorite, row.city + "," + row.country), row.numLikes, createLikeButton(row.city + "," + row.country, row.likeButton)]);

            tableManager.setHeaders(["City", "Country", "Avg Min Temp", "Avg Max Temp", "Distance", "Favorite", "Likes", ""]);
            tableManager.setRows(rawRows);
        }
    };
    
    xhttp.open("POST", requestUrl, true);
    xhttp.setRequestHeader("Content-type", "text/plain");
    xhttp.send(loc);
}
	    
	    
function createAddLikeButton(loc, num){
    var likeBtn = document.createElement("BUTTON");
    likeBtn.onclick = () => addLike(loc, num);
    likeBtn.innerHTML = "";
    likeBtn.classList.remove("remove-like");
    likeBtn.classList.add("add-like");
    likeBtn.appendChild(document.createTextNode('Add Like'));

    
    return likeBtn;
}

function sortLikes(){
	console.log("a");
    var rows = []
    var i=0
	for(result of res){
		rows.push(new row(result.city, result.country, result.avgMinTemp, result.avgMaxTemp, 
				result.distance, result.isFavorite, result.numLikes, i));
		i++;
    }
    
    if(rows.length == 0){
        displayErrorMessage("No locations found.");
        return;
    }
	
	rows.sort((a, b) => (a.numLikes < b.numLikes) ? 1 : -1);
    
    var rawRows = rows.map((row) => [row.city, row.country, row.minTemp, row.maxTemp, row.distance, createFavButton(row.isFavorite, row.city + "," + row.country), row.numLikes, createLikeButton(row.city + "," + row.country, row.likeButton)]);

    tableManager.setHeaders(["City", "Country", "Avg Min Temp", "Avg Max Temp", "Distance", "Favorite", "Likes", ""]);
    tableManager.setRows(rawRows);
    localStorage.sort = "true";
}

