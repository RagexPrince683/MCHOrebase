package com.norwood.mcheli.wrapper.modelloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class W_WavefrontObject extends W_ModelCustom {
   private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
   private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
   private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
   private static Pattern groupObjectPattern = Pattern.compile("([go]( [-\\$\\w\\d]+) *\\n)|([go]( [-\\$\\w\\d]+) *$)");
   private static Matcher vertexMatcher;
   private static Matcher vertexNormalMatcher;
   private static Matcher textureCoordinateMatcher;
   private static Matcher face_V_VT_VN_Matcher;
   private static Matcher face_V_VT_Matcher;
   private static Matcher face_V_VN_Matcher;
   private static Matcher face_V_Matcher;
   private static Matcher groupObjectMatcher;
   public ArrayList<W_Vertex> vertices = new ArrayList();
   public ArrayList<W_Vertex> vertexNormals = new ArrayList();
   public ArrayList<W_TextureCoordinate> textureCoordinates = new ArrayList();
   public ArrayList<W_GroupObject> groupObjects = new ArrayList();
   private W_GroupObject currentGroupObject;
   private String fileName;

   public W_WavefrontObject(ResourceLocation location, IResource resource) throws _ModelFormatException {
      this.fileName = resource.toString();
      this.loadObjModel(resource.func_110527_b());
   }

   public W_WavefrontObject(ResourceLocation resource) throws _ModelFormatException {
      this.fileName = resource.toString();

      try {
         IResource res = Minecraft.func_71410_x().func_110442_L().func_110536_a(resource);
         this.loadObjModel(res.func_110527_b());
      } catch (IOException var3) {
         throw new _ModelFormatException("IO Exception reading model format", var3);
      }
   }

   public W_WavefrontObject(String fileName, URL resource) throws _ModelFormatException {
      this.fileName = fileName;

      try {
         this.loadObjModel(resource.openStream());
      } catch (IOException var4) {
         throw new _ModelFormatException("IO Exception reading model format", var4);
      }
   }

   public W_WavefrontObject(String filename, InputStream inputStream) throws _ModelFormatException {
      this.fileName = filename;
      this.loadObjModel(inputStream);
   }

   public boolean containsPart(String partName) {
      Iterator var2 = this.groupObjects.iterator();

      W_GroupObject groupObject;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         groupObject = (W_GroupObject)var2.next();
      } while(!partName.equalsIgnoreCase(groupObject.name));

      return true;
   }

   private void loadObjModel(InputStream inputStream) throws _ModelFormatException {
      BufferedReader reader = null;
      String currentLine = null;
      int lineCount = 0;

      try {
         reader = new BufferedReader(new InputStreamReader(inputStream));

         while((currentLine = reader.readLine()) != null) {
            ++lineCount;
            currentLine = currentLine.replaceAll("\\s+", " ").trim();
            if (!currentLine.startsWith("#") && currentLine.length() != 0) {
               W_Vertex vertex;
               if (currentLine.startsWith("v ")) {
                  vertex = this.parseVertex(currentLine, lineCount);
                  if (vertex != null) {
                     this.checkMinMax(vertex);
                     this.vertices.add(vertex);
                  }
               } else if (currentLine.startsWith("vn ")) {
                  vertex = this.parseVertexNormal(currentLine, lineCount);
                  if (vertex != null) {
                     this.vertexNormals.add(vertex);
                  }
               } else if (currentLine.startsWith("vt ")) {
                  W_TextureCoordinate textureCoordinate = this.parseTextureCoordinate(currentLine, lineCount);
                  if (textureCoordinate != null) {
                     this.textureCoordinates.add(textureCoordinate);
                  }
               } else if (currentLine.startsWith("f ")) {
                  if (this.currentGroupObject == null) {
                     this.currentGroupObject = new W_GroupObject("Default");
                  }

                  W_Face face = this.parseFace(currentLine, lineCount);
                  if (face != null) {
                     this.currentGroupObject.faces.add(face);
                  }
               } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ") && currentLine.charAt(2) == '$') {
                  W_GroupObject group = this.parseGroupObject(currentLine, lineCount);
                  if (group != null && this.currentGroupObject != null) {
                     this.groupObjects.add(this.currentGroupObject);
                  }

                  this.currentGroupObject = group;
               }
            }
         }

         this.groupObjects.add(this.currentGroupObject);
      } catch (IOException var16) {
         throw new _ModelFormatException("IO Exception reading model format", var16);
      } finally {
         this.checkMinMaxFinal();

         try {
            reader.close();
         } catch (IOException var15) {
         }

         try {
            inputStream.close();
         } catch (IOException var14) {
         }

      }
   }

   public void renderAll() {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      if (this.currentGroupObject != null) {
         builder.begin(this.currentGroupObject.glDrawingMode, DefaultVertexFormats.field_181710_j);
      } else {
         builder.begin(4, DefaultVertexFormats.field_181710_j);
      }

      this.tessellateAll(tessellator);
      tessellator.draw();
   }

   public void tessellateAll(Tessellator tessellator) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         groupObject.render(tessellator);
      }

   }

   public void renderOnly(String... groupNames) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         String[] var4 = groupNames;
         int var5 = groupNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String groupName = var4[var6];
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render();
            }
         }
      }

   }

   public void tessellateOnly(Tessellator tessellator, String... groupNames) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         String[] var5 = groupNames;
         int var6 = groupNames.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String groupName = var5[var7];
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render(tessellator);
            }
         }
      }

   }

   public void renderPart(String partName) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         if (partName.equalsIgnoreCase(groupObject.name)) {
            groupObject.render();
         }
      }

   }

   public void tessellatePart(Tessellator tessellator, String partName) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         if (partName.equalsIgnoreCase(groupObject.name)) {
            groupObject.render(tessellator);
         }
      }

   }

   public void renderAllExcept(String... excludedGroupNames) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         boolean skipPart = false;
         String[] var5 = excludedGroupNames;
         int var6 = excludedGroupNames.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String excludedGroupName = var5[var7];
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               skipPart = true;
            }
         }

         if (!skipPart) {
            groupObject.render();
         }
      }

   }

   public void tessellateAllExcept(Tessellator tessellator, String... excludedGroupNames) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         boolean exclude = false;
         String[] var6 = excludedGroupNames;
         int var7 = excludedGroupNames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String excludedGroupName = var6[var8];
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               exclude = true;
            }
         }

         if (!exclude) {
            groupObject.render(tessellator);
         }
      }

   }

   private W_Vertex parseVertex(String line, int lineCount) throws _ModelFormatException {
      W_Vertex vertex = null;
      if (isValidVertexLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            if (tokens.length == 2) {
               return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
            } else {
               return (W_Vertex)(tokens.length == 3 ? new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertex);
            }
         } catch (NumberFormatException var6) {
            throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), var6);
         }
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private W_Vertex parseVertexNormal(String line, int lineCount) throws _ModelFormatException {
      W_Vertex vertexNormal = null;
      if (isValidVertexNormalLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            return (W_Vertex)(tokens.length == 3 ? new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertexNormal);
         } catch (NumberFormatException var6) {
            throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), var6);
         }
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private W_TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws _ModelFormatException {
      W_TextureCoordinate textureCoordinate = null;
      if (isValidTextureCoordinateLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            if (tokens.length == 2) {
               return new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
            } else {
               return (W_TextureCoordinate)(tokens.length == 3 ? new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : textureCoordinate);
            }
         } catch (NumberFormatException var6) {
            throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), var6);
         }
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private W_Face parseFace(String line, int lineCount) throws _ModelFormatException {
      W_Face face = null;
      if (isValidFaceLine(line)) {
         face = new W_Face();
         String trimmedLine = line.substring(line.indexOf(" ") + 1);
         String[] tokens = trimmedLine.split(" ");
         String[] subTokens = null;
         if (tokens.length == 3) {
            if (this.currentGroupObject.glDrawingMode == -1) {
               this.currentGroupObject.glDrawingMode = 4;
            } else if (this.currentGroupObject.glDrawingMode != 4) {
               throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
            }
         } else if (tokens.length == 4) {
            if (this.currentGroupObject.glDrawingMode == -1) {
               this.currentGroupObject.glDrawingMode = 7;
            } else if (this.currentGroupObject.glDrawingMode != 7) {
               throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
            }
         }

         int i;
         if (isValidFace_V_VT_VN_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.textureCoordinates = new W_TextureCoordinate[tokens.length];
            face.vertexNormals = new W_Vertex[tokens.length];

            for(i = 0; i < tokens.length; ++i) {
               subTokens = tokens[i].split("/");
               face.vertices[i] = (W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
               face.textureCoordinates[i] = (W_TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
               face.vertexNormals[i] = (W_Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
            }

            face.faceNormal = face.calculateFaceNormal();
         } else if (isValidFace_V_VT_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.textureCoordinates = new W_TextureCoordinate[tokens.length];

            for(i = 0; i < tokens.length; ++i) {
               subTokens = tokens[i].split("/");
               face.vertices[i] = (W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
               face.textureCoordinates[i] = (W_TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
            }

            face.faceNormal = face.calculateFaceNormal();
         } else if (isValidFace_V_VN_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.vertexNormals = new W_Vertex[tokens.length];

            for(i = 0; i < tokens.length; ++i) {
               subTokens = tokens[i].split("//");
               face.vertices[i] = (W_Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
               face.vertexNormals[i] = (W_Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
            }

            face.faceNormal = face.calculateFaceNormal();
         } else {
            if (!isValidFace_V_Line(line)) {
               throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
            }

            face.vertices = new W_Vertex[tokens.length];

            for(i = 0; i < tokens.length; ++i) {
               face.vertices[i] = (W_Vertex)this.vertices.get(Integer.parseInt(tokens[i]) - 1);
            }

            face.faceNormal = face.calculateFaceNormal();
         }

         return face;
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private W_GroupObject parseGroupObject(String line, int lineCount) throws _ModelFormatException {
      W_GroupObject group = null;
      if (isValidGroupObjectLine(line)) {
         String trimmedLine = line.substring(line.indexOf(" ") + 1);
         if (trimmedLine.length() > 0) {
            group = new W_GroupObject(trimmedLine);
         }

         return group;
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private static boolean isValidVertexLine(String line) {
      if (vertexMatcher != null) {
         vertexMatcher.reset();
      }

      vertexMatcher = vertexPattern.matcher(line);
      return vertexMatcher.matches();
   }

   private static boolean isValidVertexNormalLine(String line) {
      if (vertexNormalMatcher != null) {
         vertexNormalMatcher.reset();
      }

      vertexNormalMatcher = vertexNormalPattern.matcher(line);
      return vertexNormalMatcher.matches();
   }

   private static boolean isValidTextureCoordinateLine(String line) {
      if (textureCoordinateMatcher != null) {
         textureCoordinateMatcher.reset();
      }

      textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
      return textureCoordinateMatcher.matches();
   }

   private static boolean isValidFace_V_VT_VN_Line(String line) {
      if (face_V_VT_VN_Matcher != null) {
         face_V_VT_VN_Matcher.reset();
      }

      face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
      return face_V_VT_VN_Matcher.matches();
   }

   private static boolean isValidFace_V_VT_Line(String line) {
      if (face_V_VT_Matcher != null) {
         face_V_VT_Matcher.reset();
      }

      face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
      return face_V_VT_Matcher.matches();
   }

   private static boolean isValidFace_V_VN_Line(String line) {
      if (face_V_VN_Matcher != null) {
         face_V_VN_Matcher.reset();
      }

      face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
      return face_V_VN_Matcher.matches();
   }

   private static boolean isValidFace_V_Line(String line) {
      if (face_V_Matcher != null) {
         face_V_Matcher.reset();
      }

      face_V_Matcher = face_V_Pattern.matcher(line);
      return face_V_Matcher.matches();
   }

   private static boolean isValidFaceLine(String line) {
      return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
   }

   private static boolean isValidGroupObjectLine(String line) {
      if (groupObjectMatcher != null) {
         groupObjectMatcher.reset();
      }

      groupObjectMatcher = groupObjectPattern.matcher(line);
      return groupObjectMatcher.matches();
   }

   public String getType() {
      return "obj";
   }

   public void renderAllLine(int startLine, int maxLine) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(1, DefaultVertexFormats.field_181705_e);
      this.renderAllLine(tessellator, startLine, maxLine);
      tessellator.draw();
   }

   public void renderAllLine(Tessellator tessellator, int startLine, int maxLine) {
      int lineCnt = 0;
      BufferBuilder builder = tessellator.getBuffer();
      Iterator var6 = this.groupObjects.iterator();

      while(true) {
         W_GroupObject groupObject;
         do {
            if (!var6.hasNext()) {
               return;
            }

            groupObject = (W_GroupObject)var6.next();
         } while(groupObject.faces.size() <= 0);

         Iterator var8 = groupObject.faces.iterator();

         while(var8.hasNext()) {
            W_Face face = (W_Face)var8.next();

            for(int i = 0; i < face.vertices.length / 3; ++i) {
               W_Vertex v1 = face.vertices[i * 3 + 0];
               W_Vertex v2 = face.vertices[i * 3 + 1];
               W_Vertex v3 = face.vertices[i * 3 + 2];
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.pos((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
               builder.pos((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.pos((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
               builder.pos((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.pos((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
               builder.pos((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
            }
         }
      }
   }

   public int getVertexNum() {
      return this.vertices.size();
   }

   public int getFaceNum() {
      return this.getVertexNum() / 3;
   }

   public void renderAll(int startFace, int maxFace) {
      if (startFace < 0) {
         startFace = 0;
      }

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(4, DefaultVertexFormats.field_181710_j);
      this.renderAll(tessellator, startFace, maxFace);
      tessellator.draw();
   }

   public void renderAll(Tessellator tessellator, int startFace, int maxLine) {
      int faceCnt = 0;
      Iterator var5 = this.groupObjects.iterator();

      while(true) {
         W_GroupObject groupObject;
         do {
            if (!var5.hasNext()) {
               return;
            }

            groupObject = (W_GroupObject)var5.next();
         } while(groupObject.faces.size() <= 0);

         Iterator var7 = groupObject.faces.iterator();

         while(var7.hasNext()) {
            W_Face face = (W_Face)var7.next();
            ++faceCnt;
            if (faceCnt >= startFace) {
               if (faceCnt > maxLine) {
                  return;
               }

               face.addFaceForRender(tessellator);
            }
         }
      }
   }
}
