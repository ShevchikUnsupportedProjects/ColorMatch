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

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.material.Wool;
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

	public void setGameLevel(Location location) {
		world = location.getWorld().getName();
		p1 = location.toVector();
		p2 = p1.clone().add(new Vector(32, 0, 32));
		centralPoint = p1.clone().add(new Vector(16, 1, 16));
		regen();
	}

	public void removeAllWoolExceptColor(DyeColor color) {
		int y = p1.getBlockY();
		for (int x = p1.getBlockX() + 1; x < p2.getBlockX(); x++) {
			for (int z = p1.getBlockZ() + 1; z < p2.getBlockZ(); z++) {
				Block b = getWorld().getBlockAt(x, y, z);
				Wool wool = (Wool) b.getState().getData();
				if (wool.getColor() != color) {
					b.setType(Material.AIR);
				}
			}
		}
	}

	private Random rnd = new Random();
	private DyeColor[] colors = DyeColor.values();
	public void regen() {
		int y = p1.getBlockY();
		for (int x = p1.getBlockX() + 1; x < p2.getBlockX(); x++) {
			for (int z = p1.getBlockZ() + 1; z < p2.getBlockZ(); z++) {
				Block b = getWorld().getBlockAt(x, y, z);
				if (b.getType() != Material.WOOL) {
					b.setType(Material.WOOL);
					BlockState bs = b.getState();
					Wool wool = (Wool) bs.getData();
					wool.setColor(colors[rnd.nextInt(colors.length)]);
					bs.setData(wool);
					bs.update();
				}
			}
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
