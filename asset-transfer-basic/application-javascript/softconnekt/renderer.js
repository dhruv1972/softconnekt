const { ipcRenderer } = require('electron');
const os = require('os');


    const FingerprintJS = require('@fingerprintjs/fingerprintjs');

    async function getDeviceId() {
    const fp = await FingerprintJS.load();
    const result = await fp.get();
    return result.visitorId;
    }


document.getElementById('form-create').addEventListener('submit', async (e) => {
    e.preventDefault();

    const productKey = document.getElementById('productKey').value;
    const email = document.getElementById('email').value;
    
    const deviceID = await getDeviceId();
    const timestamp = new Date();

    try {
        const result = await ipcRenderer.invoke('setupGateway');
        if (result) {
            const assetResult = await ipcRenderer.invoke('CreateAsset', productKey, email, deviceID, timestamp);
            if (assetResult.success) {
                alert('Created License');
            } else {
                alert(assetResult.message);
                console.error(assetResult.message);
            }
        } else {
            alert('Failed to setup gateway.');
        }
    } catch (error) {
        alert('Failed to setup gateway: ' + error.message);
        console.error('Failed to setup gateway:', error);
    }
});


document.getElementById('form-delete').addEventListener('submit', async (e) => {
    e.preventDefault();

    const productKey = document.getElementById('productKey').value;
    const deviceID = document.getElementById('deviceID').value;

    try {
        const result = await ipcRenderer.invoke('setupGateway');
        if (result) {
            const assetResult = await ipcRenderer.invoke('DeleteAsset', productKey, deviceID);
            if (assetResult.success) {
                alert('Asset deleted');
            } else {
                alert(assetResult.message);
                console.error(assetResult.message);
            }
        } else {
            alert('Failed to setup gateway.');
        }
    } catch (error) {
        alert('Failed to setup gateway: ' + error.message);
        console.error('Failed to setup gateway:', error);
    }
});


document.getElementById('form-check').addEventListener('submit', async (e) => {
    e.preventDefault();

    const productKey = document.getElementById('productKey').value;
    const deviceID = document.getElementById('deviceID').value;
    
    const timestamp = new Date();

    try {
        const result = await ipcRenderer.invoke('setupGateway');
        if (result) {
            const assetResult = await ipcRenderer.invoke('AssetExists', productKey,deviceID);
            if (assetResult.success) {
                alert('Details'+"\n"+"ProductKey:"+productKey+"\n"+deviceID+"\n"+"TimeStamp:"+ timestamp)
            } else {
                alert(assetResult.message);
                console.error(assetResult.message);
            }
        } else {
            alert('Failed to setup gateway.');
        }
    } catch (error) {
        alert('Failed to setup gateway: ' + error.message);
        console.error('Failed to setup gateway:', error);
    }
});



document.getElementById('form-transfer').addEventListener('submit', async (e) => {
    e.preventDefault();

    const productKey = document.getElementById('productKey').value;
    const email = document.getElementById('email').value;
    const newemail = document.getElementById('newemail').value;
    const deviceID = document.getElementById('deviceID').value;
    
    const newdeviceID =await getDeviceId();
    const timestamp = new Date();

    try {
        const result = await ipcRenderer.invoke('setupGateway');
        if (result) {
            const assetResult = await ipcRenderer.invoke('TransferAsset', productKey, newemail, deviceID, newdeviceID, timestamp);
            if (assetResult.success) {
                alert('Created License');
            } else {
                alert(assetResult.message);
                console.error(assetResult.message);
            }
        } else {
            alert('Failed to setup gateway.');
        }
    } catch (error) {
        alert('Failed to setup gateway: ' + error.message);
        console.error('Failed to setup gateway:', error);
    }
});




