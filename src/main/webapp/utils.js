const fav = {
    addFavorite: function(btn, loc){
        var requestUrl = "/favorites"

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                btn.innerHTML = "";
                btn.classList.remove("remove-fav");
                btn.classList.add("add-fav");
                btn.id = "fav-add-" + loc;
                btn.onclick = () => fav.removeFavorite(btn, loc);
                btn.appendChild(document.createTextNode('Remove Favorite'));
            }
        };
        
        xhttp.open("POST", requestUrl, true);
        xhttp.setRequestHeader("Content-type", "text/plain");
        xhttp.send(loc);
    },
    
    removeFavorite: function(btn, loc){
        var requestUrl = "/favorites"

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                btn.innerHTML = "";
                btn.classList.remove("add-fav");
                btn.classList.add("remove-fav");
                btn.id = "fav-remove-" + loc;
                btn.onclick = () => fav.addFavorite(btn, loc);
                btn.appendChild(document.createTextNode('Add Favorite'));
            }
        };
        
        xhttp.open("DELETE", requestUrl, true);
        xhttp.setRequestHeader("Content-type", "text/plain");
        xhttp.send(loc);
    },
    
    createAddButton: function(loc){
        var favBtn = document.createElement("BUTTON");
        favBtn.onclick = () => fav.addFavorite(favBtn, loc);
        favBtn.innerHTML = "";
        favBtn.classList.remove("remove-fav");
        favBtn.classList.add("add-fav");
        favBtn.id = "fav-add-" + loc;
        favBtn.appendChild(document.createTextNode('Add Favorite'));

        return favBtn;
    },

    createRemoveButton: function(loc){
        var favBtn = document.createElement("BUTTON");
        favBtn.onclick = () => fav.removeFavorite(favBtn, loc);
        favBtn.innerHTML = "";
        favBtn.classList.remove("add-fav");
        favBtn.classList.add("remove-fav");
        favBtn.id = "fav-remove-" + loc;
        favBtn.appendChild(document.createTextNode('Remove Favorite'));

        return favBtn;
    }
}

/*const like = {
	    addLike: function(loc, num){
	        var requestUrl = "/likes"

	        var xhttp = new XMLHttpRequest();
	        xhttp.onreadystatechange = function() {
	            if (this.readyState == 4 && this.status == 200) {
	                res[num].numLikes = res[num].numLikes+1;
	                displayResults(res);
	            }
	        };
	        
	        xhttp.open("POST", requestUrl, true);
	        xhttp.setRequestHeader("Content-type", "text/plain");
	        xhttp.send(loc);
	    },
	    
	    
	    createAddLikeButton: function(loc, num){
	        var likeBtn = document.createElement("BUTTON");
	        likeBtn.onclick = () => addLike(loc, num);
	        likeBtn.innerHTML = "";
	        likeBtn.classList.remove("remove-like");
	        likeBtn.classList.add("add-like");
	        likeBtn.appendChild(document.createTextNode('Add Like'));

	        return likeBtn;
	    },

	}*/
const user = {
	logIn: function(){
        var body = {
            username: usernameIn.value,
            password: passwordIn.value
        }

        var requestUrl = "/login"

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                switch (this.status) {
                    case 201:
                        modal.close();
                        break;
                    case 404:
                        modal.setMessage("User does not exist.");
                        break;
                    case 401:
                        modal.setMessage("Incorrect password!");
                        break;
                    case 405:
                        modal.setMessage("Invalid username.");
                        break;
                    case 406:
                        modal.setMessage("Invalid password.");
                        break;    
                    case 500:
                        modal.setMessage("Server error. Try again later.");
                        break;
                    default:
                        break;
                }
            }
        };
        
        xhttp.open("POST", requestUrl, true);
        xhttp.setRequestHeader("Content-type", "application/json; charset=UTF-8");
        xhttp.send(JSON.stringify(body));
    },
    createUser: function(){
        var body = {
            username: usernameIn.value,
            password: passwordIn.value
        }

        var requestUrl = "/users"

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                switch (this.status) {
                    case 201:
                        modal.close();
                        break;
                    case 409:
                        modal.setMessage("User already exists.");
                        break;
                    case 500:
                        modal.setMessage("Server error. Try again later.");
                        break;
                    default:
                        break;
                }
            }
        };
        
        xhttp.open("POST", requestUrl, true);
        xhttp.setRequestHeader("Content-type", "application/json; charset=UTF-8");
        xhttp.send(JSON.stringify(body));
    }
}


function formatLocation(loc){
	return loc.replace(" ", "+").split("/")[0];
}

const locFinder = {
    supported: function(){
        if(navigator.geolocation){
            return true;
        }
        return false;
    },
    
    getLocation: function(callback){
        navigator.geolocation.getCurrentPosition((location) => this.processBrowserLoc(location, callback), console.log);
    },
    processBrowserLoc: function(location, callback){
        const lat = location.coords.latitude;
        const long = location.coords.longitude;

        var requestUrl = "https://us1.locationiq.com/v1/reverse.php?key=01980d650edf2c" + 
        "&lat=" + lat + 
        "&lon=" + long + "&format=json";

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                callback(JSON.parse(xhttp.response)['address']);
            }
        };
        
        xhttp.open("GET", requestUrl, true);
        xhttp.setRequestHeader("Content-type", "text/plain");
        xhttp.send();
    },
    autoFillInput(input){
        this.getLocation((location) => {
            input.value = location.city
        });
    }
}

var usernameIn;
var passwordIn;
var modalElement;

document.addEventListener("DOMContentLoaded", function(){
    usernameIn = document.getElementById("username");
    passwordIn = document.getElementById("password");
    modalElement = document.getElementById("modal");
});


const modal = {
    show: function(){
        modalElement.style.display = "flex";
    },
    close: function(){
        modalElement.style.display = "none";
        this.setMessage("");
    },
    setMessage: function(message){
        document.getElementById("log-in-message").innerHTML = message;
    }
}

class TableManager {
    constructor(tableElement) {
        this.table = tableElement;
        this.body = tableElement.getElementsByTagName("tbody")[0];
        this.headers = [];
    }

    setHeaders(headers){
        this.table.deleteTHead();
        const head = this.table.createTHead();
        const headRow = head.insertRow(0);

        for (const header of headers) {
            const th = document.createElement("th");
            th.innerHTML = header;
            headRow.appendChild(th);
        }

        this.headers = headers.slice();
    }

    clearContent(){
        while(this.table.rows.length > 1){
            this.table.deleteRow(this.table.rows.length - 1)
        }
    }

    addRow(row){
        function isDomElement(element) {
            return element instanceof Element || element instanceof HTMLDocument;  
        }

        const rowElement = this.body.insertRow()
        for (const element of row) {
            rowElement.insertCell().appendChild(isDomElement(element) ? element : document.createTextNode(element));
        }
    }

    addRows(rows){
        for (const row of rows) {
            this.addRow(row)
        }
    }

    setRows(rows){
        this.clearContent();
        this.addRows(rows);
    }

    sortByHeader(header){
        if(!this.headers.includes(header)){
            throw new Error("header is not a table header")
        }

        var rows = []
        for (const row of this.table.rows) {
            var rowList = [];

            for(var i = 0; i < this.headers.length; i += 1){
                rowList.push(row.cells[i].innerHTML);
            }

            rows.push(rowList);
        }

        const index = this.headers.indexOf(header);

        rows.sort((a, b) => (a[index] > b[index]) ? 1 : -1);

        this.clearContent();
        this.addRows(rows);
    }
}