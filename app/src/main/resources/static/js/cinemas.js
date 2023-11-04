document.addEventListener('DOMContentLoaded', ageLimit());

function ageLimit() {

    const ages = document.querySelectorAll(".ageLimit");

    if(ages.length!=0){
        ages.forEach((age, index1) =>{

            const a = age.innerHTML;
            let num =  parseFloat(a);

            if(num >= 17){
                age.style.backgroundColor = '#FF0000';
            }else if(num < 17 && num >= 12){
                age.style.backgroundColor = '#FF8C00';
            }else if(num<12){
                age.style.backgroundColor = '#04BF8A';
            }

        })
    }

}


document.getElementById("selectedDate").onchange = changeMovies;
document.getElementById("selectedGenre").onchange = changeMovies;

function changeMovies() {

     var selectedDate = document.getElementById("selectedDate").value;
     var selectedGenre = document.getElementById("selectedGenre").value;
     var category = document.getElementById("category").value;

     var request = new XMLHttpRequest();

     request.open("POST", "/cinemas?date=" + selectedDate + "&genre=" + selectedGenre + "&category=" + category, true);

     request.onreadystatechange = function () {
         if (request.readyState === 4) {
             if (request.status === 200) {
                 var response = request.responseText;
                 var container = document.getElementById("contentContainer");
                 container.innerHTML = response;
                 ageLimit();
             }
             else {
                 console.error("Hiba történt a kérés során.");
             }
         }
     };

     request.send();

}



