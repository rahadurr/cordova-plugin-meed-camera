var exec = require('cordova/exec');


/**
 * This method will return all permissions that required
 * for access camera hardware.
 * 
 * options = [
 *  Permission.Camera,
 *  Permission.Audio
 * ]
 */
exports.getPermission = function (options, success, error) {
    exec(success, error, 'Camera', 'getPermission', [options]);
};

/**
 * This method will capture photo by front or back camera
 * and return File URI for captured photo
 * 
 * options = {
 *  camera: Cameras.Front,
 *  formate: Formate.Png
 *  response: Response.Path 
 * }
 */
exports.capturePhoto = function (options, success, error) {
    exec(success, error, 'Camera', 'capturePhoto', [options]);
};

/**
 * This method will record video by front or back camera
 * and return File URI for captured photo
 * 
 * options = {
 *  camera: Cameras.Front,
 *  formate: Formate.MP4
 *  audio: true,
 *  response: Response.Path 
 * }
 */
exports.recordVideo = function (options, success, error) {
    exec(success, error, 'Camera', 'recordVideo', [options]);
};


exports.openSetting = function (options, success, error) {
    exec(success, error, 'Camera', 'openSetting', [options]);
};