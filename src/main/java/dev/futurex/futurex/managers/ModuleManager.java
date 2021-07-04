package dev.futurex.futurex.managers;

import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.module.modules.bot.Milo;
import dev.futurex.futurex.module.modules.client.*;
import dev.futurex.futurex.module.modules.combat.*;
import dev.futurex.futurex.module.modules.misc.*;
import dev.futurex.futurex.module.modules.movement.*;
import dev.futurex.futurex.module.modules.player.*;
import dev.futurex.futurex.module.modules.render.*;
import dev.futurex.futurex.util.client.color.ThemeColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 11/13/20
 */

public class ModuleManager implements MixinInterface {
	public ModuleManager() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	static List<Module> modules = Arrays.asList(
			// client
			new Baritone(),
			new Capes(),
			new ClickGUI(),
			new Colors(),
			new Console(),
			new ClientFont(),
			new HUD(),
			new Social(),

			// combat
			new AimBot(),
			new AntiCrystal(),
			new Aura(),
			new AutoArmor(),
			new AutoBed(),
			new AutoCity(),
			new AutoCrystal(),
			new AutoTotem(),
			new AutoTrap(),
			new Burrow(),
			new Criticals(),
			new HoleFill(),
			new Offhand(),
			new QuickBow(),
			new QuickEXP(),
			new Quiver(),
			new SelfTrap(),
			new Surround(),
			new Trigger(),
			new Web(),

			// player
			new AutoMine(),
			new Blink(),
			new ExtraSlots(),
			new FastPlace(),
			new HandProgress(),
			new LiquidInteract(),
			new NoEntityTrace(),
			new NoRotate(),
			new PacketEat(),
			new Reach(),
			new Rubberband(),
			new SpeedMine(),
			new Swing(),

			// misc
			new AntiAFK(),
			new AntiPacketKick(),
			new AutoDisconnect(),
			new AutoFish(),
			new BuildHeight(),
			new ChatLogger(),
			new ChatSuffix(),
			new ColoredText(),
			new DiscordRPC(),
			new EnableMessage(),
			new FakeGameMode(),
			new FakePlayer(),
			new MiddleClickFriend(),
			new Notifier(),
			new Portal(),
			new SkinBlinker(),
			new StashFinder(),
			new Timer(),

			// movement
			new AirJump(),
			new Anchor(),
			new AntiLevitation(),
			new AntiVoid(),
			new AutoJump(),
			new AutoWalk(),
			new BoatFlight(),
			new ElytraFlight(),
			new Flight(),
			new HighJump(),
			new IceSpeed(),
			new Jesus(),
			new NoFall(),
			new NoSlow(),
			new Parkour(),
			new ReverseStep(),
			new RotationLock(),
			new SafeWalk(),
			new Scaffold(),
			new Speed(),
			new Sprint(),
			new Step(),
			new Velocity(),
			new WebTeleport(),

			// render
			new BlockHighlight(),
			new BreakESP(),
			new CityESP(),
			new CrossHairs(),
			new CustomFOV(),
			new ESP(),
			new FullBright(),
			new HoleESP(),
			new ItemPreview(),
			new NameTags(),
			new NoBob(),
			new NoRender(),
			new Skeleton(),
			new SkyColor(),
			new StorageESP(),
			new Tracers(),
			new Trajectories(),
			new VoidESP(),
			new Weather(),

			// bot
			new Milo()
	);

	public static List<Module> getModules(){
		return new ArrayList<>(modules);
	}

	public static List<Module> getModulesInCategory(Module.Category cat){
		List<Module> module = new ArrayList<>();

		for (Module m : modules) {
			if (m.getCategory().equals(cat))
				module.add(m);
		}

		return module;
	}

	public static Module getModuleByName(String name) {
		return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public static Module getModuleByClass(Class<?> clazz) {
		return modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		ModuleManager.onUpdate();
		ModuleManager.keyListen();
		ThemeColor.updateColors();
	}

	@SubscribeEvent
	public void onFastTick(TickEvent event) {
		ModuleManager.onFastUpdate();
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		ModuleManager.onServerUpdate();
	}

	// TODO: add null checks to every override so the try catch in this is unneccesary
	public static void onUpdate() {
		for (Module m : modules) {
			try {
				if (m.isEnabled())
					m.onUpdate();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public static void onFastUpdate() {
		for (Module m : modules) {
			try {
				if (m.isEnabled())
					m.onFastUpdate();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public static void onServerUpdate() {
		for (Module m : modules) {
			try {
				if (m.isEnabled())
					m.onServerUpdate();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public static void keyListen() {
		if (mc.currentScreen == null) {
			for (Module m : modules) {
				try {
					if (Keyboard.isKeyDown(Keyboard.KEY_NONE) || Keyboard.isKeyDown(Keyboard.CHAR_NONE))
						return;

					if (Keyboard.isKeyDown(m.getKeybind().getKeyCode()) && !m.isKeyDown) {
						m.isKeyDown = true;
						m.toggle();
					}

					else if (!Keyboard.isKeyDown(m.getKeybind().getKeyCode()))
						m.isKeyDown = false;

				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}
}