package de.polarwolf.ragnarok.orchestrator;

import org.bukkit.plugin.Plugin;

import de.polarwolf.libsequence.exception.LibSequenceException;
import de.polarwolf.libsequence.logger.LibSequenceLoggerDefault;
import de.polarwolf.libsequence.orchestrator.LibSequenceSequencer;
import de.polarwolf.ragnarok.actions.RagnarokActionBlockNewLogins;
import de.polarwolf.ragnarok.actions.RagnarokActionCancel;
import de.polarwolf.ragnarok.actions.RagnarokActionKickall;
import de.polarwolf.ragnarok.actions.RagnarokActionShutdown;
import de.polarwolf.ragnarok.shutdown.RagnarokHelper;
import de.polarwolf.ragnarok.shutdown.RagnarokListener;
import de.polarwolf.ragnarok.shutdown.RagnarokSequence;

public class RagnarokOrchestrator {

	private final Plugin plugin;
	private final LibSequenceSequencer sequenceAPI;

	private final LibSequenceLoggerDefault logger;
	private final RagnarokConfig ragnarokConfig;
	private final RagnarokHelper ragnarokHelper;
	private final RagnarokSequence ragnarokSequence;
	private final RagnarokListener ragnarokListener;

	protected RagnarokOrchestrator(Plugin plugin, LibSequenceSequencer sequenceAPI) throws LibSequenceException {
		this.plugin = plugin;
		this.sequenceAPI = sequenceAPI;
		this.logger = createLogger();
		this.ragnarokConfig = createRagnarokConfig();
		setDebug(ragnarokConfig.isDebug());
		this.ragnarokHelper = createRagnarokHelper();
		this.ragnarokSequence = createRagnarokSequence();
		startup();
		this.ragnarokListener = createRagnarokListener();
	}

	// Getters
	public Plugin getPlugin() {
		return plugin;
	}

	public LibSequenceSequencer getSequenceAPI() {
		return sequenceAPI;
	}

	public LibSequenceLoggerDefault getLogger() {
		return logger;
	}

	public RagnarokConfig getRagnarokConfig() {
		return ragnarokConfig;
	}

	public RagnarokHelper getRagnarokHelper() {
		return ragnarokHelper;
	}

	public RagnarokSequence getRagnarokSequence() {
		return ragnarokSequence;
	}

	protected RagnarokListener getRagnarokListener() {
		return ragnarokListener;
	}

	// Generators
	protected LibSequenceLoggerDefault createLogger() {
		return new LibSequenceLoggerDefault(plugin);
	}

	protected RagnarokConfig createRagnarokConfig() {
		return new RagnarokConfig(this);
	}

	protected RagnarokHelper createRagnarokHelper() {
		return new RagnarokHelper(this);
	}

	protected RagnarokSequence createRagnarokSequence() {
		return new RagnarokSequence(this);
	}

	protected RagnarokListener createRagnarokListener() {
		return new RagnarokListener(this);
	}

	// Initialize
	protected void startup() throws LibSequenceException {
		ragnarokSequence.registerAction("block-new-logins", new RagnarokActionBlockNewLogins(ragnarokHelper));
		ragnarokSequence.registerAction("kickall", new RagnarokActionKickall(ragnarokHelper));
		ragnarokSequence.registerAction("shutdown", new RagnarokActionShutdown(ragnarokHelper));
		ragnarokSequence.registerAction("cancelshutdown", new RagnarokActionCancel(ragnarokHelper));
		ragnarokSequence.registerSequences();
	}

	// Reload
	public void setDebug(boolean newDebug) {
		logger.setEnableConsoleNotifications(newDebug);
		logger.setEnableInitiatorNotifications(newDebug);
	}

	public void reload() throws LibSequenceException {
		try {
			ragnarokSequence.reload();
		} finally {
			setDebug(ragnarokConfig.isDebug());
		}
	}

	// Shutdown
	public boolean isDisabled() {
		return ragnarokHelper.isDisabled();
	}

	protected void disable() {
		if (!isDisabled()) {
			ragnarokListener.disable();
			ragnarokSequence.disable();
		}
	}

}
