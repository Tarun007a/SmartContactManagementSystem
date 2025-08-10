console.log("script loaded")

let currentTheme = getTheme();

//initially change will be automatically called at first
document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});


function changeTheme(){
    document.querySelector('html').classList.add(currentTheme);

    //set the listener to change theme button
    const changeThemeButton = document.querySelector("#theme_change_button");

    changeThemeButton.addEventListener("click", (event) => {
        console.log("change theme button clicked");
        const oldTheme = currentTheme;
        console.log("change button clicked");
        //light to dark and dark to light
        if(currentTheme == "dark"){
            currentTheme = "light";
        }
        else{
            currentTheme = "dark";
        }

        //update in localStorage
        setTheme(currentTheme);

        //remove the current theme
        document.querySelector("html").classList.remove(oldTheme);

        //set current theme
        document.querySelector("html").classList.add(currentTheme);

        //change the text of button
        changeThemeButton.querySelector('span').textContent = currentTheme == "light" ? "Dark" : "Light";
    })
}

function setTheme(theme){
    localStorage.setItem("theme", theme);
}

function getTheme(){
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}