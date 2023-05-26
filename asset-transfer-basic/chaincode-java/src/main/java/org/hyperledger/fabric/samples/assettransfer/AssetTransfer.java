/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(name = "basic", info = @Info(title = "Asset Transfer", description = "The hyperlegendary asset transfer", version = "0.0.1-SNAPSHOT", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(email = "a.transfer@example.com", name = "Adrian Transfer", url = "https://hyperledger.example.com")))
@Default
public final class AssetTransfer implements ContractInterface {

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        CreateAsset(ctx, "VK7JG-NPHTM-C97JM-9MPGT-3V66T", "user1@example.com", "deviceID1", "2023-05-25T11:00:00Z");
        CreateAsset(ctx, "DPH2V-TTNVB-4X9Q3-TJR4H-KHJW4", "user2@example.com", "deviceID2", "2023-05-25T12:00:00Z");
        CreateAsset(ctx, "W269N-WFGWX-YVC9B-4J6C9-T83GX", "user3@example.com", "deviceID3", "2023-05-25T13:00:00Z");
        CreateAsset(ctx, "MH37W-N47XK-V7XM9-C7227-GCQG9", "user4@example.com", "deviceID4", "2023-05-25T14:00:00Z");
        CreateAsset(ctx, "X9XDM-98N7V-6WMQ6-BX7FG-H8Q99", "user5@example.com", "deviceID5", "2023-05-25T15:00:00Z");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Asset CreateAsset(final Context ctx, final String licenseKey, final String email, final String deviceID, final String timestamp) {
        ChaincodeStub stub = ctx.getStub();

        if (AssetExists(ctx, licenseKey, deviceID)) {
            String errorMessage = String.format("Asset %s already exists", licenseKey);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        Asset asset = new Asset(licenseKey, email, deviceID, timestamp);

        String sortedJson = genson.serialize(asset);
        stub.putStringState(licenseKey, sortedJson);

        return asset;
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Asset ReadAsset(final Context ctx, final String licenseKey, final String deviceID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(licenseKey + deviceID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist for deviceID %s", licenseKey, deviceID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset asset = genson.deserialize(assetJSON, Asset.class);
        return asset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String licenseKey, final String deviceID) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, licenseKey, deviceID)) {
            String errorMessage = String.format("Asset %s does not exist for deviceID %s", licenseKey, deviceID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(licenseKey);
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String licenseKey, final String deviceID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(licenseKey);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

	/*
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void TransferAsset(final Context ctx, final String licenseKey, final String newEmail, final String newDeviceID, final String DeviceID, final String newtimeStamp) {
    	ChaincodeStub stub = ctx.getStub();
    
    	DeleteAsset(ctx, licenseKey ,DeviceID);
    	CreateAsset(ctx, licenseKey, newEmail, newDeviceID, newtimeStamp);
    
    }
	*/
	@Transaction(intent = Transaction.TYPE.SUBMIT)
    public void TransferAsset(final Context ctx, final String licenseKey, final String newEmail, final String newDeviceID, final String deviceID, final String newTimestamp) {
    	ChaincodeStub stub = ctx.getStub();
    	
    	if (!AssetExists(ctx, licenseKey, deviceID)) {
            String errorMessage = String.format("Asset %s does not exist for deviceID %s", licenseKey, deviceID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }
        
        Asset asset = new Asset(licenseKey, newEmail, newDeviceID, newTimestamp);

        String sortedJson = genson.serialize(asset);
        stub.putStringState(licenseKey, sortedJson);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Asset> queryResults = new ArrayList<Asset>();


        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            Asset asset = genson.deserialize(result.getStringValue(), Asset.class);
            System.out.println(asset);
            queryResults.add(asset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }
}
