const { app, BrowserWindow, ipcMain } = require('electron');
const { Gateway, Wallets } = require('fabric-network');
const FabricCAServices = require('fabric-ca-client');
const path = require('path');
const { buildCAClient, registerAndEnrollUser, enrollAdmin } = require('../../../test-application/javascript/CAUtil.js');
const { buildCCPOrg1, buildWallet } = require('../../../test-application/javascript/AppUtil.js');

const channelName = 'mychannel';
const chaincodeName = 'basic';
const mspOrg1 = 'Org1MSP';
const walletPath = path.join(__dirname, 'wallet');
const org1UserId = 'javascriptAppUser';

let mainWindow;
let contract = null;

async function createWindow () {
    mainWindow = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
            enableRemoteModule: true,
        }
    });

    mainWindow.loadFile('index.html');
    mainWindow.webContents.openDevTools();

    mainWindow.on('closed', function () {
        mainWindow = null;
    });
}

app.whenReady().then(createWindow);

app.on('window-all-closed', function () {
    if (process.platform !== 'darwin') app.quit();
});

app.on('activate', function () {
    if (mainWindow === null) createWindow();
});

async function setupGateway() {
    try {
        const ccp = buildCCPOrg1();
        const wallet = await buildWallet(Wallets, walletPath);
        let gateway = new Gateway();

        await gateway.connect(ccp, {
            wallet,
            identity: org1UserId,
            discovery: { enabled: true, asLocalhost: true }
        });

        const network = await gateway.getNetwork(channelName);
        contract = network.getContract(chaincodeName);

        return true;
    } catch (error) {
        console.error(`**** FAILED to setup gateway: ${error}`);
        return false;
    }
}

ipcMain.handle('setupGateway', async (event, args) => {
    return await setupGateway();
});

ipcMain.handle('CreateAsset', async (event, productKey, email, deviceID, timestamp) => {
    try {
        await contract.submitTransaction('CreateAsset', productKey, email, deviceID, timestamp);
        return { success: true };
    } catch (error) {
        return { success: false, message: error.message };
    }
});

ipcMain.handle('DeleteAsset', async (event, productKey, deviceID) => {
    try {
        await contract.submitTransaction('DeleteAsset', productKey, deviceID);
        return { success: true };
    } catch (error) {
        return { success: false, message: error.message };
    }
});

ipcMain.handle('AssetExists', async (event, productKey, deviceID) => {
    try {
        const result = await contract.evaluateTransaction('AssetExists', productKey, deviceID);
        return { success: true, data: result.toString() };
    } catch (error) {
        return { success: false, message: error.message };
    }
});

ipcMain.handle('TransferAsset', async (event, productKey, newemail, deviceID, newdeviceID, timestamp) => {
    try {
        await contract.submitTransaction('TransferAsset', productKey, newemail, deviceID, newdeviceID, timestamp);
        return { success: true };
    } catch (error) {
        return { success: false, message: error.message };
    }
});

