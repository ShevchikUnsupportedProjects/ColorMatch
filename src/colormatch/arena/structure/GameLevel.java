/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package colormatch.arena.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class GameLevel {

	private Vector p1;
	private Vector p2;
	private Vector centralPoint;
	public Vector getSpawnPoint() {
		return centralPoint;
	}
	private String world;
	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	private List<Block> blocks = new ArrayList<Block>(1200);
	public void cacheBlocks() {
		clearBlocks();
		int y = p1.getBlockY();
		for (int x = p1.getBlockX() + 1; x < p2.getBlockX(); x++) {
			for (int z = p1.getBlockZ() + 1; z < p2.getBlockZ(); z++) {
				blocks.add(getWorld().getBlockAt(x, y, z));
			}
		}
	}
	public void clearBlocks() {
		blocks.clear();
	}

	public void setGameLevel(Location location) {
		world = location.getWorld().getName();
		p1 = location.toVector();
		p2 = p1.clone().add(new Vector(32, 0, 32));
		centralPoint = p1.clone().add(new Vector(16, 1, 16));
		cacheBlocks();
		regen();
	}

	@SuppressWarnings("deprecation")
	public void removeAllWoolExceptColor(byte cd) {
		for (Block block : blocks) {
			if (block.getData() != cd) {
				block.setTypeIdAndData(0, (byte) 0, false);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private final int CLAY_ID = Material.STAINED_CLAY.getId();
	private final byte[] randomColorsArray = new byte[7100];
	{
		byte[] colors = new byte[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		Random rnd = new Random();
		for (int i = 0; i < randomColorsArray.length; i++) {
			randomColorsArray[i] = colors[rnd.nextInt(colors.length)];
		}
	}

	private int randomCounter = 0;
	@SuppressWarnings("deprecation")
	public void regen() {
		for (Block block : blocks) {
			randomCounter++;
			if (randomCounter >= randomColorsArray.length) {
				randomCounter = 0;
			}
			block.setTypeIdAndData(CLAY_ID, randomColorsArray[randomCounter], false);
		}
	}

	public void saveToConfig(FileConfiguration config) {
		config.set("centralpoint", centralPoint);
		config.set("p1", p1);
		config.set("p2", p2);
		config.set("world", world);
	}

	public void loadFromConfig(FileConfiguration config) {
		centralPoint = config.getVector("centralpoint");
		p1 = config.getVector("p1");
		p2 = config.getVector("p2");
		world = config.getString("world");
	}

}
