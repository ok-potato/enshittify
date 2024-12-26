// state

/**
 * @type {Number}
*/
var currentTrackIdx = undefined
/**
 * @type {Boolean}
 */
var paused = true

// play button

/**
 * @type {Element}
 */
const playButton = document.getElementsByClassName("play-button")[0]
const playIcon = document.getElementsByClassName("play-icon")[0]

playButton.addEventListener("click", event => {
    if (!paused) {
        pause()
    } else if (currentTrackIdx == undefined) {
        switchTrackTo(0)
    } else {
        switchTrackTo(currentTrackIdx)
    }
})

// track list items + selection
const trackListItems = Array.from(document.getElementsByClassName("track"))
const loadedTracks = new Array(trackListItems.length)

/**
 * @type {Element}
*/
var selectedItem = undefined

document.addEventListener("click", event => {
    if (event.detail === 1) { unselect() }
})

trackListItems.forEach((item, idx) => {
    // don't select text on double/triple click
    item.addEventListener("mousedown", event => {
        if (event.detail === 2 || event.detail === 3) { event.preventDefault() }
    })

    item.addEventListener("click", event => {
        // single click
        if (event.detail === 1) {
            unselect()
            selectedItem = item
            item.classList.add("selected")
        }
        // double click
        if (event.detail === 2) { switchTrackTo(idx) }

        if (event.detail === 1 || event.detail === 2) { event.stopPropagation() }
    })
});

function unselect() {
    if (selectedItem != undefined) { selectedItem.classList.remove("selected") }
}

// track playing

/**
 * @param {Number} trackIdx
 */
function switchTrackTo(trackIdx) {
    // if there's a track playing, pause it if it's the selected track, and reset it otherwise
    if (!paused) {
        if (currentTrackIdx == trackIdx) {
            pause()
            return
        }
        loadedTracks[currentTrackIdx].pause()
        loadedTracks[currentTrackIdx].currentTime = 0
    } else if (currentTrackIdx != undefined) {
        if (currentTrackIdx != trackIdx) {
            loadedTracks[currentTrackIdx].pause()
            loadedTracks[currentTrackIdx].currentTime = 0
        }
    }
    if (currentTrackIdx != undefined) {
        trackListItems[currentTrackIdx].classList.remove("playing")
    }
    currentTrackIdx = undefined

    enqueue(trackIdx)
}

function pause() {
    loadedTracks[currentTrackIdx].pause()
    stop()
}

function cancelPlayback() {
    stop()
}

function stop() {
    paused = true

    document.title = document.getElementsByClassName("banner-release")[0].textContent

    playIcon.src = "/play.svg"
    playIcon.classList.remove("play-icon-pause")
    playIcon.classList.add("play-icon-play")
}

function enqueue(trackIdx) {
    // fetch the new track if it isn't already in memory
    const newTrack = (loadedTracks[trackIdx] != undefined) ? loadedTracks[trackIdx] : new Audio(window.location.href + "/" + (trackIdx + 1))

    // play the track if it's ready (i.e. already in memory) - otherwise, set up callbacks
    if (newTrack.readyState == HTMLMediaElement.HAVE_ENOUGH_DATA) {
        play(newTrack, trackIdx)
    } else {
        newTrack.oncanplaythrough = event => { play(newTrack, trackIdx) }
        newTrack.onerror = event => {
            console.log("cancel playback")
            newTrack.oncanplaythrough = event => { }
            cancelPlayback()
            if (trackIdx < loadedTracks.length - 1) {
                switchTrackTo(trackIdx + 1)
            }
        }
    }
}

function play(track, trackIdx) {
    track.oncanplaythrough = event => { }
    track.onended = event => { switchTrackTo(trackIdx + 1) }
    currentTrackIdx = trackIdx
    loadedTracks[trackIdx] = track
    track.play()

    paused = false

    document.title = trackListItems[trackIdx].querySelector(".track-title").textContent

    trackListItems[trackIdx].classList.add("playing")

    playIcon.src = "/pause.svg"
    playIcon.classList.remove("play-icon-play")
    playIcon.classList.add("play-icon-pause")
}

window.onbeforeunload = event => {
    stop()
}