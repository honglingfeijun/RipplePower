package org.ripple.power.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.json.JSONObject;
import org.ripple.power.config.LSystem;
import org.ripple.power.i18n.LangConfig;
import org.ripple.power.txns.AccountFind;
import org.ripple.power.txns.AccountInfo;
import org.ripple.power.txns.AccountLine;
import org.ripple.power.txns.IssuedCurrency;
import org.ripple.power.txns.NameFind;
import org.ripple.power.txns.Payment;
import org.ripple.power.txns.Rollback;
import org.ripple.power.txns.Updateable;
import org.ripple.power.utils.MathUtils;
import org.ripple.power.utils.SwingUtils;
import org.ripple.power.wallet.WalletCache;
import org.ripple.power.wallet.WalletItem;

public class RPIOUSendDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RPCButton _sendButton;
	private RPCButton _exitButton;
	private RPComboBox _curList;
	private RPLabel _feeLabel;
	private RPLabel _currencyLabel;
	private RPLabel _addressLabel;
	private RPLabel _amountLabel;
	private javax.swing.JSeparator jSeparator1;
	private RPTextBox _feeText;
	private RPTextBox _addressText;
	private RPTextBox _amountText;

	private final WalletItem _walletItem;

	public static void showDialog(String name, JFrame parent, WalletItem item) {
		try {
			RPIOUSendDialog dialog = new RPIOUSendDialog(name, parent, item,
					"", "1", LSystem.FEE);
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public static void showDialog(String name, JFrame parent, WalletItem item,
			String address, String amount, String fee) {
		try {
			RPIOUSendDialog dialog = new RPIOUSendDialog(name, parent, item,
					address, amount, fee);
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public RPIOUSendDialog(String text, JFrame parent, final WalletItem item,
			String address, String amount, String fee) {
		super(parent, text, Dialog.ModalityType.DOCUMENT_MODAL);
		setResizable(false);
		Dimension dim = new Dimension(575, 270);
		setPreferredSize(dim);
		setSize(dim);

		_walletItem = item;

		getContentPane().setBackground(LSystem.dialogbackground);
		_feeLabel = new RPLabel();
		_curList = new RPComboBox();
		_sendButton = new RPCButton();
		jSeparator1 = new javax.swing.JSeparator();
		_currencyLabel = new RPLabel();
		_addressLabel = new RPLabel();
		_feeText = new RPTextBox();
		_addressText = new RPTextBox();
		_amountLabel = new RPLabel();
		_amountText = new RPTextBox();
		_exitButton = new RPCButton();

		getContentPane().setLayout(null);

		Font font = new Font(LangConfig.fontName, 0, 14);

		_feeLabel.setFont(font); // NOI18N
		_feeLabel.setText(LangConfig.get(this, "fee", "Fee"));
		getContentPane().add(_feeLabel);
		_feeLabel.setBounds(10, 130, 70, 16);

		_curList.setFont(font); // NOI18N
		_curList.setItemModel(new String[] { "Empty" });
		getContentPane().add(_curList);
		_curList.setBounds(80, 10, 470, 21);

		_sendButton.setText(LangConfig.get(this, "send", "Send"));
		_sendButton.setFont(font);
		getContentPane().add(_sendButton);
		_sendButton.setBounds(370, 200, 90, 30);
		_sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String address = _addressText.getText().trim();
				String amount = _amountText.getText().trim();
				String fee = _feeText.getText().trim();
				Object o = _curList.getSelectedItem();
				if (!address.startsWith("~")) {
					if (!address.startsWith("r") || address.length() < 31) {
						RPMessage.showErrorMessage(LSystem.applicationMain,
								"Error", "无效的Ripple地址!");
						return;
					}
				}
				if (!MathUtils.isNan(amount)) {
					RPMessage.showErrorMessage(LSystem.applicationMain,
							"Error", "无效的发币数量!");
					return;
				}
				if (!MathUtils.isNan(fee)) {
					RPMessage.showErrorMessage(LSystem.applicationMain,
							"Error", "无效的手续费数量!");
					return;
				}
				IssuedCurrency cur = null;
				if (o instanceof String) {
					cur = new IssuedCurrency((String) o);
				} else if (o instanceof IssuedCurrency) {
					cur = (IssuedCurrency) o;
				} else if (o instanceof AccountLine) {
					AccountLine line = (AccountLine) o;
					cur = new IssuedCurrency(amount, line.getIssuer(), line
							.getCurrency());
					Double a = Double.parseDouble(amount);
					Double b = Double.parseDouble(line.getAmount());
					if (a > b) {
						RPMessage.showWarningMessage(LSystem.applicationMain,
								"Warning", "发送失败,您的IOU货币量不足以完成本次发送!");
						return;
					}
				} else {
					RPMessage.showWarningMessage(LSystem.applicationMain,
							"Warning", "发送失败,无法获得当前Address数据!");
					return;
				}
				if (address.startsWith("~")) {
					try {
						address = NameFind.getAddress(address);
					} catch (Exception e1) {
						RPMessage.showWarningMessage(LSystem.applicationMain,
								"Warning", "发送失败,无法获得当前地址数据!");
					}
					if (address == null) {
						RPMessage.showWarningMessage(LSystem.applicationMain,
								"Warning", "发送失败,无法获得当前地址数据!");
					}
				}
				final WaitDialog dialog = WaitDialog
						.showDialog(RPIOUSendDialog.this);
				Payment.send(item.getSeed(), address, cur, fee, new Rollback() {

					@Override
					public void success(JSONObject res) {
						JSonLog.get().println(res.toString());
						WalletCache.get().reset();
						RPMessage.showInfoMessage(LSystem.applicationMain,
								"Info", "发送完毕.");
						dialog.closeDialog();
					}

					@Override
					public void error(JSONObject res) {
						JSonLog.get().println(res.toString());
						dialog.closeDialog();
					}
				});
			}
		});

		getContentPane().add(jSeparator1);
		jSeparator1.setBounds(0, 180, 570, 10);

		_currencyLabel.setFont(font); // NOI18N
		_currencyLabel.setText(LangConfig.get(this, "currency", "Currency"));
		getContentPane().add(_currencyLabel);
		_currencyLabel.setBounds(10, 10, 80, 16);

		_addressLabel.setFont(font); // NOI18N
		_addressLabel.setText(LangConfig.get(this, "address", "Address"));
		getContentPane().add(_addressLabel);
		_addressLabel.setBounds(10, 50, 70, 16);

		_feeText.setFont(font); // NOI18N
		getContentPane().add(_feeText);
		_feeText.setBounds(80, 130, 160, 22);
		_feeText.setText(fee);

		_addressText.setFont(font); // NOI18N
		getContentPane().add(_addressText);
		_addressText.setBounds(80, 50, 470, 22);
		_addressText.setText(address);

		_amountLabel.setFont(font); // NOI18N
		_amountLabel.setText(LangConfig.get(this, "amount", "Amount"));
		getContentPane().add(_amountLabel);
		_amountLabel.setBounds(10, 90, 70, 16);

		_amountText.setFont(font); // NOI18N
		getContentPane().add(_amountText);
		_amountText.setBounds(80, 90, 300, 22);
		_amountText.setText(amount);

		_exitButton.setText(LangConfig.get(this, "exit", "Exit"));
		_exitButton.setFont(font);
		getContentPane().add(_exitButton);
		_exitButton.setBounds(470, 200, 90, 30);
		_exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtils.close(RPIOUSendDialog.this);
			}
		});
		calldisable();
		if (_walletItem != null) {
			loadIOUs(_walletItem.getPublicKey());
		}
		pack();
	}

	public void calldisable() {
		_sendButton.setEnabled(false);
		_feeText.setEnabled(false);
		_addressText.setEnabled(false);
		_amountText.setEnabled(false);
	}

	public void callactivity() {
		_sendButton.setEnabled(true);
		_feeText.setEnabled(true);
		_addressText.setEnabled(true);
		_amountText.setEnabled(true);
	}

	private AccountInfo loadIOUs(String address) {
		final WaitDialog dialog = WaitDialog.showDialog(this);
		final AccountInfo info = new AccountInfo();
		Updateable accountline = new Updateable() {
			@Override
			public void action(Object res) {
				if (info.lines.size() > 0) {
					_curList.setItemModel(info.lines.toArray());
					callactivity();
				} else {
					calldisable();
				}
				dialog.closeDialog();
			}
		};
		AccountFind find = new AccountFind();
		find.processLines(address, info, accountline);
		return info;
	}

	public WalletItem getWalletItem() {
		return _walletItem;
	}

}
