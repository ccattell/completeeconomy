package me.ccattell.plugins.completeeconomy.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author charlie
 */
public class CEToolBoost {

    List<Integer> spades = Arrays.asList(new Integer[]{256, 269, 273, 277, 284});
    List<Integer> pickaxes = Arrays.asList(new Integer[]{257, 270, 274, 278, 285});
    List<Integer> axes = Arrays.asList(new Integer[]{258, 271, 275, 279, 286});
    List<Integer> swords = Arrays.asList(new Integer[]{267, 268, 272, 276, 283});
    List<Integer> hoes = Arrays.asList(new Integer[]{290, 291, 292, 293, 294});
    List<Integer> hand = Arrays.asList(new Integer[]{0});
    List<Integer> shears = Arrays.asList(new Integer[]{359});
    // what about buckets for lava/water ?
    // what about enchanted tools like silk touch that let you get blocks like ORES ?
    HashMap<Integer, List<Integer>> lookup = new HashMap<Integer, List<Integer>>();

    public CEToolBoost() {
//AIR(0, 0)
        lookup.put(1, pickaxes); //STONE
        lookup.put(2, spades); //GRASS
        lookup.put(3, spades); //DIRT
        lookup.put(4, pickaxes); //COBBLESTONE
        lookup.put(5, axes); //WOOD
//SAPLING(6, Tree.class)
//BEDROCK(7)
//WATER(8, MaterialData.class)
//STATIONARY_WATER(9, MaterialData.class)
//LAVA(10, MaterialData.class)
//STATIONARY_LAVA(11, MaterialData.class);
        lookup.put(12, spades); //SAND
        lookup.put(13, spades); //GRAVEL
        lookup.put(14, pickaxes); //GOLD_ORE
        lookup.put(15, pickaxes); //IRON_ORE
        lookup.put(16, pickaxes); //COAL_ORE
        lookup.put(17, axes); //LOG
        lookup.put(18, shears); //LEAVES
//SPONGE(19);
        lookup.put(20, hand); //GLASS
        lookup.put(21, pickaxes); //LAPIS_ORE
        lookup.put(22, pickaxes); //LAPIS_BLOCK
        lookup.put(23, pickaxes); //DISPENSER
        lookup.put(24, pickaxes); //SANDSTONE
        lookup.put(25, axes); //NOTE_BLOCK
//BED_BLOCK(26, Bed.class);
        lookup.put(27, pickaxes); //POWERED_RAIL
        lookup.put(28, pickaxes); //DETECTOR_RAIL
//PISTON_STICKY_BASE(29, PistonBaseMaterial.class);
        lookup.put(30, swords); //WEB
        lookup.put(31, hoes); //LONG_GRASS
        lookup.put(32, hoes); //DEAD_BUSH
//PISTON_BASE(33, PistonBaseMaterial.class)
//PISTON_EXTENSION(34, PistonExtensionMaterial.class);
        lookup.put(35, shears); //WOOL
//PISTON_MOVING_PIECE(36);
        lookup.put(37, hand); //YELLOW_FLOWER
        lookup.put(38, hand); //RED_ROSE
        lookup.put(39, hoes); //BROWN_MUSHROOM
        lookup.put(40, hoes); //RED_MUSHROOM
        lookup.put(41, pickaxes); //GOLD_BLOCK
        lookup.put(42, pickaxes); //IRON_BLOCK
        lookup.put(43, axes); //DOUBLE_STEP
        lookup.put(44, spades); //STEP
        lookup.put(45, pickaxes); //BRICK
        lookup.put(46, spades); //TNT
        lookup.put(47, axes); //BOOKSHELF
        lookup.put(48, pickaxes); //MOSSY_COBBLESTONE
        lookup.put(49, pickaxes); //OBSIDIAN
        lookup.put(50, spades); //TORCH
//FIRE(51);
        lookup.put(52, pickaxes); //MOB_SPAWNER
        lookup.put(53, axes); //WOOD_STAIRS
        lookup.put(54, axes); //CHEST
        lookup.put(55, spades); //REDSTONE_WIRE
        lookup.put(56, pickaxes); //DIAMOND_ORE
        lookup.put(57, pickaxes); //DIAMOND_BLOCK
        lookup.put(58, axes); //WORKBENCH
        lookup.put(59, hoes); //CROPS
        lookup.put(60, hoes); //SOIL
        lookup.put(61, pickaxes); //FURNACE
        lookup.put(62, pickaxes); //BURNING_FURNACE
        lookup.put(63, axes); //SIGN_POST
        lookup.put(64, axes); //WOODEN_DOOR
        lookup.put(65, axes); //LADDER
        lookup.put(66, pickaxes); //RAILS
        lookup.put(67, pickaxes); //COBBLESTONE_STAIRS
        lookup.put(68, axes); //WALL_SIGN
        lookup.put(69, axes); //LEVER
        lookup.put(70, pickaxes); //STONE_PLATE
        lookup.put(71, pickaxes); //IRON_DOOR_BLOCK
        lookup.put(72, axes); //WOOD_PLATE
        lookup.put(73, pickaxes); //REDSTONE_ORE
//GLOWING_REDSTONE_ORE(74);
        lookup.put(75, spades); //REDSTONE_TORCH_OFF
        lookup.put(76, spades); //REDSTONE_TORCH_ON
        lookup.put(77, pickaxes); //STONE_BUTTON
        lookup.put(78, spades); //SNOW
        lookup.put(79, spades); //ICE
        lookup.put(80, spades); //SNOW_BLOCK
        lookup.put(81, hoes); //CACTUS
        lookup.put(82, spades); //CLAY
        lookup.put(83, hoes); //SUGAR_CANE_BLOCK
        lookup.put(84, axes); //JUKEBOX
        lookup.put(85, axes); //FENCE
        lookup.put(86, hoes); //PUMPKIN
        lookup.put(87, pickaxes); //NETHERRACK
        lookup.put(88, spades); //SOUL_SAND
        lookup.put(89, pickaxes); //GLOWSTONE
//PORTAL(90);
        lookup.put(91, hoes); //JACK_O_LANTERN
        lookup.put(92, hand); //CAKE_BLOCK
        lookup.put(93, axes); //DIODE_BLOCK_OFF
        lookup.put(94, axes); //DIODE_BLOCK_ON
//LOCKED_CHEST(95);
        lookup.put(96, axes); //TRAP_DOOR
        lookup.put(97, pickaxes); //MONSTER_EGGS
        lookup.put(98, pickaxes); //SMOOTH_BRICK
        lookup.put(99, hoes); //HUGE_MUSHROOM_1
        lookup.put(100, hoes); //HUGE_MUSHROOM_2
        lookup.put(101, pickaxes); //IRON_FENCE
        lookup.put(102, hand); //THIN_GLASS
        lookup.put(103, hoes); //MELON_BLOCK
        lookup.put(104, hoes); //PUMPKIN_STEM
        lookup.put(105, hoes); //MELON_STEM
        lookup.put(106, shears); //VINE
        lookup.put(107, axes); //FENCE_GATE
        lookup.put(108, pickaxes); //BRICK_STAIRS
        lookup.put(109, pickaxes); //SMOOTH_STAIRS
        lookup.put(110, spades); //MYCEL
        lookup.put(111, hoes); //WATER_LILY
        lookup.put(112, pickaxes); //NETHER_BRICK
        lookup.put(113, pickaxes); //NETHER_FENCE
        lookup.put(114, pickaxes); //NETHER_BRICK_STAIRS
        lookup.put(115, hoes); //NETHER_WARTS
        lookup.put(116, hand); //ENCHANTMENT_TABLE
        lookup.put(117, hand); //BREWING_STAND
        lookup.put(118, hand); //CAULDRON
//ENDER_PORTAL(119)
        lookup.put(120, pickaxes); //ENDER_PORTAL_FRAME
        lookup.put(121, pickaxes); //ENDER_STONE
        lookup.put(122, pickaxes); //DRAGON_EGG
        lookup.put(123, hand); //REDSTONE_LAMP_OFF
        lookup.put(124, hand); //REDSTONE_LAMP_ON
        lookup.put(125, axes); //WOOD_DOUBLE_STEP
        lookup.put(126, axes); //WOOD_STEP
        lookup.put(127, hoes); //COCOA
        lookup.put(128, pickaxes); //SANDSTONE_STAIRS
        lookup.put(129, pickaxes); //EMERALD_ORE
        lookup.put(130, pickaxes); //ENDER_CHEST
        lookup.put(131, hand); //TRIPWIRE_HOOK
        lookup.put(132, hand); //TRIPWIRE
        lookup.put(133, pickaxes); //EMERALD_BLOCK
        lookup.put(134, axes); //SPRUCE_WOOD_STAIRS
        lookup.put(135, axes); //BIRCH_WOOD_STAIRS
        lookup.put(136, axes); //JUNGLE_WOOD_STAIRS
//COMMAND(137, Command.class);
        lookup.put(138, pickaxes); //BEACON
        lookup.put(139, pickaxes); //COBBLE_WALL
        lookup.put(140, hoes); //FLOWER_POT
        lookup.put(141, hoes); //CARROT
        lookup.put(142, hoes); //POTATO
        lookup.put(143, axes); //WOOD_BUTTON
        lookup.put(144, swords); //SKULL
        lookup.put(145, pickaxes); //ANVIL
        lookup.put(146, axes); //TRAPPED_CHEST
        lookup.put(147, pickaxes); //GOLD_PLATE
        lookup.put(148, pickaxes); //IRON_PLATE
        lookup.put(149, axes); //REDSTONE_COMPARATOR_OFF
        lookup.put(150, axes); //REDSTONE_COMPARATOR_ON
        lookup.put(151, axes); //DAYLIGHT_DETECTOR
        lookup.put(152, pickaxes); //REDSTONE_BLOCK
        lookup.put(153, pickaxes); //QUARTZ_ORE
        lookup.put(154, pickaxes); //HOPPER
        lookup.put(155, pickaxes); //QUARTZ_BLOCK
        lookup.put(156, pickaxes); //QUARTZ_STAIRS
        lookup.put(157, pickaxes); //ACTIVATOR_RAIL
        lookup.put(158, pickaxes); //DROPPER
    }

    public HashMap<Integer, List<Integer>> getLookup() {
        return lookup;
    }
}
