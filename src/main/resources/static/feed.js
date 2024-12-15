// artists

var addArtistButton = document.getElementById("add-artist")
var artistsFieldSet = document.getElementsByClassName("form-artists")[0]
var artistNr = 0

addArtistInput()

addArtistButton.addEventListener("click", event => addArtistInput())

function addArtistInput() {
    artistNr++
    artistsFieldSet.insertAdjacentHTML("beforeend",
        `<input id="artist-${artistNr}" type="text" name="artist-${artistNr}" placeholder="Artist ${artistNr}">`
    )
}

// cover art

/**
 * @type {HTMLLabelElement}
 */
const coverArtSelect = document.getElementById("cover-art-select")
/**
 * @type {HTMLImageElement}
 */
const coverArtPreview = document.getElementById("cover-art-preview")
/**
 * @type {HTMLInputElement}
 */
const coverArtInput = document.getElementById("cover-art")

const fileReader = new FileReader()
fileReader.onloadend = () => {
    coverArtPreview.src = fileReader.result
    coverArtPreview.style.padding = "0"
}

displayPreview()

/**
 * @param {Event} event 
 */
function displayPreview() {
    const file = coverArtInput.files[0]
    if (file != undefined) {
        fileReader.readAsDataURL(file)
    }
}

// tracks

var addTrackButton = document.getElementById("add-track")
var trackFieldSet = document.getElementsByClassName("form-tracks")[0]
var trackNr = 0

addTrackInput()
addTrackInput()
addTrackInput()
addTrackInput()
addTrackInput()

addTrackButton.addEventListener("click", event => addTrackInput())

function addTrackInput() {
    trackNr++
    const div = document.createElement("div")
    div.setAttribute("id", `track-${trackNr}`)
    div.classList.add("upload-track")
    
    div.style.display = "flex"
    div.style.width = "min(100%, 50rem)"

    const titleInput = document.createElement("input")
    titleInput.setAttribute("type", "text")
    titleInput.setAttribute("id", `track-name-${trackNr}`)
    titleInput.setAttribute("name", `track-name-${trackNr}`)
    titleInput.setAttribute("placeholder", `Track ${trackNr}`)
    titleInput.classList.add("upload-track-name")
    titleInput.style.flex = "1"

    const fileInputLabel = document.createElement("label")
    fileInputLabel.textContent = "select file"
    fileInputLabel.classList.add("upload-track-impostor")
    fileInputLabel.setAttribute("for", `track-file-${trackNr}`)

    const fileInput = document.createElement("input")
    fileInput.setAttribute("type", "file")
    fileInput.setAttribute("accept", "audio/mpeg")
    fileInput.setAttribute("id", `track-file-${trackNr}`)
    fileInput.setAttribute("name", `track-file-${trackNr}`)
    fileInput.classList.add("upload-track-file")
    fileInput.onchange = (event) => displayTrackFileName(fileInputLabel, event.target.value.split("\\").findLast(element => true))

    div.appendChild(titleInput)
    div.appendChild(fileInputLabel)
    div.appendChild(fileInput)

    trackFieldSet.appendChild(div)
}

function displayTrackFileName(label, fileName) {
    if (fileName == undefined || fileName == null) {
        label.textContent = "select file"
    } else {
        label.textContent = fileName
    }
}