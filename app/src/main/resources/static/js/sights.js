const stars = document.querySelectorAll("i");

if(stars.length!=0){
    stars.forEach((star, index1) =>{

        star.addEventListener("click", () => {
            stars.forEach((star, index2) =>{

                if(index1>=index2){
                    star.classList.add("active");
                }else{
                    star.classList.remove("active");
                    star.style.color = "black";
                }

            })
        })

    })

    stars.forEach((star, index1) =>{


        star.addEventListener("mouseover", () => {
            stars.forEach((star, index2) =>{

                if(index1 >= index2 && !star.classList.contains("active")){
                        star.style.color = "#FFD700";
                }
            })
        })

        star.addEventListener("mouseout", () => {
                stars.forEach((star, index2) =>{

                    if(index1 >= index2 && !star.classList.contains("active")){
                            star.style.color= "black";

                    }
                })
            })

    })

    document.addEventListener('DOMContentLoaded', function() {

        const ratingInput = document.getElementById('rating');

        stars.forEach(function(icon) {
          icon.addEventListener('click', function() {
            const value = this.getAttribute('data-value');
            ratingInput.value = value;
          });
        });

        document.getElementById('reviewForm').addEventListener('submit', function(event) {

          if (ratingInput.value === '0') {
              event.preventDefault();
              const ratingError = document.getElementById('ratingError');
              ratingError.innerText = 'Kérlek, válassz ki egy értéket!';
          }
       });

    });

}

document.addEventListener('DOMContentLoaded', function() {

    const rateInner = document.getElementById('ratings').innerHTML;
    const rate = document.getElementById('ratings');

    let num = parseFloat(rateInner.replace(',', '.'));

    if(num >= 4.5){
        rate.style.backgroundColor = '#04BF8A';
    }else if(num < 4.5 && num >= 3.5){
        rate.style.backgroundColor = '#FF8C00';
    }else if(num<3.5){
        rate.style.backgroundColor = '#FF0000';
    }

})



