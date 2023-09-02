document.addEventListener('DOMContentLoaded', function() {

    const ages = document.querySelectorAll("#ageLimit");

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

})

/*document.getElementById('selectedDate').addEventListener('change', function() {
    var selectedDate = this.value;

    // Itt használj AJAX vagy Fetch API-t az adatok elküldéséhez a szervernek
    // Például Fetch API használata:
    fetch('/cinemas?selectedDate=' + selectedDate)
        .then(response => response.text())
        .then(data => {
            // Adataidra való frissítés
            document.getElementById('contentContainer').innerHTML = data;
        });
});*/