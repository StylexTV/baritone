/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.launch;

import com.google.common.base.Strings;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Based on GradleStart from ForgeGradle 2.3
 *
 * @author Brady
 * @since 3/11/2019
 */
public class LaunchTesting {

    public static void main(String[] args) {
        Map<String, String> arguments = new HashMap<>();

        hackNatives();
        arguments.put("version", "BaritownedDeveloperEnvironment");
        arguments.put("assetIndex", System.getenv("assetIndex"));
        arguments.put("assetsDir", System.getenv().getOrDefault("assetDirectory", "assets"));
        arguments.put("accessToken", "FML");
        arguments.put("userProperties", "{}");
        arguments.put("tweakClass", System.getenv("tweakClass"));

        List<String> argsArray = new ArrayList<>();
        arguments.forEach((k, v) -> {
            argsArray.add("--" + k);
            argsArray.add(v);
        });

        Launch.main(argsArray.toArray(new String[0]));
    }

    private static void hackNatives() {
        String paths = System.getProperty("java.library.path");
        String nativesDir = System.getenv().get("nativesDirectory");

        if (Strings.isNullOrEmpty(paths))
            paths = nativesDir;
        else
            paths += File.pathSeparator + nativesDir;

        System.setProperty("java.library.path", paths);

        // hack the classloader now.
        try
        {
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch(Throwable ignored) {}
    }
}
