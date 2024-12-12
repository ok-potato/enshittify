var addArtistButton = document.getElementById("add-artist")
var artistFormGroup = document.getElementsByClassName("form-artists")[0]
var artistNr = 1

addArtistButton.addEventListener("click", event => {
    artistNr++
    artistFormGroup.insertAdjacentHTML("beforeend", 
        `<input id="artist-${artistNr}" type="text" name="artist" placeholder="Artist ${artistNr}">`
    )
})

var addTrackButton = document.getElementById("add-track")
var trackFormGroup = document.getElementsByClassName("form-tracks")[0]
var trackNr = 3

addTrackButton.addEventListener("click", event => {
    trackNr++
    trackFormGroup.insertAdjacentHTML("beforeend", 
        `<input id="track-${trackNr}-file" type="file" name="track-file"><input id="track-${trackNr}-name" type="text" name="track-name" placeholder="Track ${trackNr}">`
    )
})

/**
 * @type {HTMLImageElement}
 */
const coverArtPreview = document.getElementById("cover-art-preview")
/**
 * @type {HTMLInputElement}
 */
const coverArtInput = document.getElementById("cover-art")

const fileReader = new FileReader()
fileReader.onloadend = () => coverArtPreview.src = fileReader.result

/**
 * @param {Event} event 
 */
function displayPreview() {
    const file = coverArtInput.files[0]
    fileReader.readAsDataURL(file)
}