package cn.wangyudi.superrespawn.sql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;

/**
 * @author AbstractPrinter
 */
public class Dber {
    private final Connection connection;
    private final JavaPlugin plugin;

    public Dber(JavaPlugin plugin, Connection connection) {
        this.plugin = plugin;
        this.connection = connection;
    }

    public Dber insertLoc(String name, Location loc) {
        try {
            String sql = "INSERT INTO srspawn_locations(spawn_name,spawn_world,spawn_x,spawn_y,spawn_z,spawn_pitch,spawn_yaw) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, loc.getWorld().getName());
            pst.setDouble(3, loc.getX());
            pst.setDouble(4, loc.getY());
            pst.setDouble(5, loc.getZ());
            pst.setFloat(6, loc.getPitch());
            pst.setFloat(7, loc.getYaw());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber insertWeight(String name, Integer weights) {
        try {
            String sql = "INSERT INTO srspawn_weights(spawn_name,spawn_weight) VALUES(?,?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, weights);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber insertPrompt(String name) {
        try {
            String sql = "INSERT INTO srspawn_prompts(spawn_name) VALUES(?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber updatePromptChat(String name, String chat) {
        try {
            String sql = "UPDATE srspawn_prompts SET prompt_chat=? WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, chat);
            pst.setString(2, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber updatePromptTitle(String name, String title) {
        try {
            String sql = "UPDATE srspawn_prompts SET prompt_title=? WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber updatePromptSubTitle(String name, String subtitle) {
        try {
            String sql = "UPDATE srspawn_prompts SET prompt_subtitle=? WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, subtitle);
            pst.setString(2, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }


    public Dber updateLoc(String name, Location loc) {
        try {
            String sql = "UPDATE srspawn_locations SET spawn_world=?,spawn_x=?,spawn_y=?,spawn_z=?,spawn_pitch=?,spawn_yaw=? WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, loc.getWorld().getName());
            pst.setDouble(2, loc.getX());
            pst.setDouble(3, loc.getY());
            pst.setDouble(4, loc.getZ());
            pst.setFloat(5, loc.getPitch());
            pst.setFloat(6, loc.getYaw());
            pst.setString(7, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber updateWeight(String name, Integer weight) {
        try {
            PreparedStatement pst = connection.prepareStatement("UPDATE srspawn_weights SET spawn_weight=? WHERE spawn_name=?");
            pst.setInt(1, weight);
            pst.setString(2, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber delLoc(String name) {
        try {
            String sql = "DELETE FROM srspawn_locations WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber delWeight(String name) {
        try {
            String sql = "DELETE FROM srspawn_weights WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public Dber delPrompt(String name){
        try {
            String sql = "DELETE FROM srspawn_prompts WHERE spawn_name=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return this;
    }

    public HashMap<String, Location> getRespawnLocations() {
        try {
            HashMap<String, Location> respawnLocations = new HashMap<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM srspawn_locations");
            while (rs.next()) {
                respawnLocations.put(rs.getString("spawn_name"), new Location(
                        Bukkit.getWorld(rs.getString("spawn_world")),
                        rs.getDouble("spawn_x"),
                        rs.getDouble("spawn_y"),
                        rs.getDouble("spawn_z"),
                        rs.getFloat("spawn_yaw"),
                        rs.getFloat("spawn_pitch")
                ));
            }
            statement.close();
            return respawnLocations;
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return null;
    }

    public HashMap<String, Integer> getRespawnWeights() {
        try {
            HashMap<String, Integer> respawnWeights = new HashMap<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM srspawn_weights");
            while (rs.next()) {
                respawnWeights.put(rs.getString("spawn_name"), rs.getInt("spawn_weight"));
            }
            statement.close();
            return respawnWeights;
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return null;
    }

    public HashMap<String, String[]> getRespawnPrompts() {
        try {
            HashMap<String, String[]> respawnPrompts = new HashMap<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM srspawn_prompts");
            while (rs.next()) {
                String[] nr = new String[3];
                nr[0] = rs.getString("prompt_chat");
                nr[1] = rs.getString("prompt_title");
                nr[2] = rs.getString("prompt_subtitle");
                respawnPrompts.put(rs.getString("spawn_name"), nr);
            }
            rs.close();
            statement.close();
            return respawnPrompts;
        } catch (SQLException throwables) {
            plugin.getLogger().info(throwables.getMessage());
        }
        return null;
    }


}
