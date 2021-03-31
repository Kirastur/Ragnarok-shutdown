package de.polarwolf.ragnarok.main;

import de.polarwolf.ragnarok.api.RagnarokAPI;

public class RagnarokProvider {

	private static RagnarokAPI ragnarokAPI;
	
	private RagnarokProvider() {
	}

    protected static void setAPI (RagnarokAPI newAPI) {
    	ragnarokAPI=newAPI;
    }
    
    public static RagnarokAPI getAPI() {
    	return ragnarokAPI;
    }
    
}
