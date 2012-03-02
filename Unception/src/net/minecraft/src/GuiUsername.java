package net.minecraft.src;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiUsername extends GuiScreen {

	private GuiScreen parentScreen;
	private GuiTextField usernameTextField;
	private GuiTextField passwordTextField;
	private String error;
	
	public GuiUsername(GuiScreen guiscreen)
	{
		parentScreen = guiscreen;
	}
	
	public GuiUsername(GuiMultiplayer guiscreen) {
		// TODO Auto-generated constructor stub
	}

	public void updateScreen()
	{
		usernameTextField.updateCursorCounter();
		passwordTextField.updateCursorCounter();
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
protected void actionPerformed(GuiButton guibutton)
{
	if(!guibutton.enabled)
	{
		return;
	}
	if(guibutton.id == 1)
	{
		mc.displayGuiScreen(parentScreen);
	}else
		if(guibutton.id == 0)
		{
			if(passwordTextField.getText().length() > 0)
			{
				String s = usernameTextField.getText();
				String s1 = passwordTextField.getText();
				try
				{
					String s2 = (new StringBuilder("user-")).append(URLEncoder.encode(s, "UTF-8")).append("&version-").append(13).toString();
					String s3 = excutePost("https://login.minecraft.net/", s2);
					if(s3 == null || !s3.contains(":"))
					{
						error = s3;
						return;
					}
					String as[] = s3.split(":");
					mc.session = new Session(as[2].trim(), as[3].trim());
					mc.displayGuiScreen(parentScreen);
				}
				catch(Exception exception)
				{
					exception.printStackTrace();
				}
			} else
		   {
				mc.session = new Session(usernameTextField.getText(), "");
		   }
			mc.displayGuiScreen(parentScreen);
		}
	}
protected void keyTyped(char c, int i)
{
	usernameTextField.textboxKeyTyped(c, i);
	passwordTextField.textboxKeyTyped(c, i);
	if(c == '\t')
	{
		if(usernameTextField.isFocused)
		{
			usernameTextField.isFocused = false;
			passwordTextField.isFocused = true;
		}else
		{
			usernameTextField.isFocused = true;
			passwordTextField.isFocused = false;
		}
	}
	if(c == '\r')
	{
		actionPerformed((GuiButton)controlList.get(0));
	}
}

protected void mouseClicked(int i, int j, int k)
{
	super.mouseClicked(i, j, k);
	usernameTextField.mouseClicked(i, j, k);
	passwordTextField.mouseClicked(i, j, k);
}
public static String excutePost(String s, String s1)
{
	HttpsURLConnection httpsurlconnection = null;
	try
	{
		try
		{
			URL url = new URL(s);
			httpsurlconnection = (HttpsURLConnection)url.openConnection();
			httpsurlconnection.setRequestMethod("POST");
			httpsurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpsurlconnection.setRequestProperty("Content-Length", Integer.toString(s1.getBytes().length));
			httpsurlconnection.setRequestProperty("Content-Laungauge", "en-US");
			httpsurlconnection.setUseCaches(false);
			httpsurlconnection.setDoInput(true);
			httpsurlconnection.setDoOutput(true);
			httpsurlconnection.connect();
			DataOutputStream dataoutputstream = new DataOutputStream(httpsurlconnection.getOutputStream());
			dataoutputstream.writeBytes(s1);
			dataoutputstream.flush();
			dataoutputstream.close();
			InputStream inputstream = httpsurlconnection.getInputStream();
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
			StringBuffer stringbuffer = new StringBuffer();
			String s2;
			while((s2 = bufferedreader.readLine()) != null)
			{
				stringbuffer.append(s2);
				stringbuffer.append('\r');
			}
			bufferedreader.close();
			String s3 = stringbuffer.toString();
			String s4 = s3;
			return s4;
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		return null;
	}
	finally
	{
		if(httpsurlconnection !=null)
		{
			httpsurlconnection.disconnect();
		}
	}
}
public void initGui()
{
	Keyboard.enableRepeatEvents(true);
	controlList.clear();
	controlList.add(new GuiButton(0, width /2 - 100, height / 4 + 96 + 12, "Finsh"));
	controlList.add(new GuiButton(1, width /2 - 100, height / 4 + 120 + 12, "Cancel"));
	usernameTextField = new GuiTextField(this, fontRenderer, width / 2 - 100, 76, 200, 20, "");
	passwordTextField = new GuiTextField(this, fontRenderer, width / 2 - 100, 116, 200, 20, "");
	usernameTextField.setMaxStringLength(16);
}

public void drawScreen(int i, int j, float f)
{
	drawDefaultBackground();
	drawCenteredString(fontRenderer, "\2477Login In With Your Account Stuff To Change Account", width / 2, (height / 4 -60) + 20, 0xffffff);
	drawString(fontRenderer, "\2477Username", width / 2 - 100, 63, 0xa0a0a0);
	drawString(fontRenderer, "\2477Password \2475(Leave Blank To Change Name)", width / 2 - 100, 104, 0xa0a0a0);
	usernameTextField.drawTextBox();
	passwordTextField.drawTextBox();
	if(error != null)
	{
		drawCenteredString(fontRenderer, (new StringBuilder("\2475Login Failed:")).append(error).toString(), width / 2, height / 4 + 72 + 12, 0xffffff);
	}
	super.drawScreen(i, j, f);
}
}
