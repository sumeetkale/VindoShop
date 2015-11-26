package com.shopping.vindoshop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.windowsazure.storage.CloudStorageAccount;
import com.microsoft.windowsazure.storage.StorageException;
import com.microsoft.windowsazure.storage.blob.BlobContainerPermissions;
import com.microsoft.windowsazure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.windowsazure.storage.blob.CloudBlobClient;
import com.microsoft.windowsazure.storage.blob.CloudBlobContainer;
import com.microsoft.windowsazure.storage.blob.CloudBlockBlob;

public class azureFileHandler {
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;"
			+ "AccountName=your_storage_account;"
			+ "AccountKey=your_storage_account_key";

	void uploadFile(File source) throws InvalidKeyException,
			URISyntaxException, StorageException, FileNotFoundException,
			IOException {// Define the connection-string with your values
		// Retrieve storage account from connection-string.
		CloudStorageAccount storageAccount = CloudStorageAccount
				.parse(storageConnectionString);

		// Create the blob client.
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		// Get a reference to a container.
		// The container name must be lower case
		CloudBlobContainer container = blobClient
				.getContainerReference("mycontainer");

		// Create the container if it does not exist.
		container.createIfNotExists();
		// Create a permissions object.
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

		// Include public access in the permissions object.
		containerPermissions
				.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

		// Set the permissions on the container.
		container.uploadPermissions(containerPermissions);

		// Define the path to a local file.

		// Create or overwrite the "myimage.jpg" blob with contents from a
		// local file.
		CloudBlockBlob blob = container.getBlockBlobReference("myimage.jpg");
		blob.upload(new FileInputStream(source), source.length());
	}

	/*
	 * Change the container permission to public blob, upload the image. Then it
	 * will be available on the url (here I'm assuming your container is called
	 * image):
	 * http://YOURSTORAGEACCOUNTHERE.blob.core.windows.net/image/azure.png
	 */

}
