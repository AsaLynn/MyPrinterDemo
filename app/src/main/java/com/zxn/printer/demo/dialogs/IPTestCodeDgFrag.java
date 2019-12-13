package com.zxn.printer.demo.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.printer.PrintExecutor;
import com.zxn.printer.PrintSocketHolder;
import com.zxn.printer.PrinterWriter;
import com.zxn.printer.PrinterWriter80mm;
import com.zxn.printer.demo.R;
import com.zxn.printer.demo.data.CodeDataMaker;
import com.zxn.printer.demo.util.InputMethodUtils;
import com.zxn.printer.util.StrUtil;

/**
 * 地址选择对话框Fragment
 * Created by Alex on 2015/11/14.
 */
public class IPTestCodeDgFrag extends DialogFragment {

    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_WIDTH = "width";
    private static final String EXTRA_HEIGHT = "height";
    private static final String EXTRA_QR = "qr";
    private IPTestDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int type = getArguments().getInt(EXTRA_TYPE, PrinterWriter80mm.TYPE_80);
        int width = getArguments().getInt(EXTRA_WIDTH, 500);
        int height = getArguments().getInt(EXTRA_HEIGHT, PrinterWriter.HEIGHT_PARTING_DEFAULT);
        String qr = getArguments().getString(EXTRA_QR);
        dialog = new IPTestDialog(getActivity(), type, width, height, qr);
        return dialog;
    }

    class IPTestDialog extends AppCompatDialog implements View.OnClickListener,
            PrintSocketHolder.OnStateChangedListener, PrintExecutor.OnPrintResultListener {

        private int type;
        private EditText edtIp;
        private EditText edtPort;
        private TextView tvState;
        private Button btnPrint;

        private PrintExecutor executor;
        private CodeDataMaker maker;

        @SuppressWarnings("all")
        IPTestDialog(Context context, int type, int width, int height, String qr) {
            super(context);
            this.type = type;
            setContentView(R.layout.dlg_printer_ip_test);

            edtIp = (EditText) findViewById(R.id.printer_edt_ip);
            edtPort = (EditText) findViewById(R.id.printer_edt_port);
            tvState = (TextView) findViewById(R.id.printer_tv_state);
            btnPrint = (Button) findViewById(R.id.printer_btn_test_print);
            btnPrint.setOnClickListener(this);
            setEditable(true);
            maker = new CodeDataMaker(context, qr, width, height);
        }

        private void setEditable(boolean editable) {
            edtIp.setEnabled(editable);
            edtPort.setEnabled(editable);
            btnPrint.setEnabled(editable);
        }

        private void setState(final int resId) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvState.setText(resId);
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.printer_btn_test_print:
                    print();
                    break;
            }
        }

        private void print() {
            //edtIp.setText("192.168.5.212");
            String ip = edtIp.getText().toString().trim();
            if (ip.length() <= 0) {
                Toast.makeText(getContext(), R.string.printer_edit_toast_1, Toast.LENGTH_SHORT).show();
                InputMethodUtils.openInputMethod(edtIp);
                return;
            } else if (!StrUtil.isIp(ip)) {
                Toast.makeText(getContext(), R.string.printer_edit_toast_2, Toast.LENGTH_SHORT).show();
                edtIp.setText(null);
                InputMethodUtils.openInputMethod(edtIp);
                return;
            }
            int port;
            String portStr = edtPort.getText().toString().trim();
            if (portStr.length() <= 0) {
                Toast.makeText(getContext(), R.string.printer_edit_toast_3, Toast.LENGTH_SHORT).show();
                InputMethodUtils.openInputMethod(edtPort);
                return;
            } else {
                try {
                    port = Integer.valueOf(portStr);
                } catch (Exception e) {
                    port = -1;
                }
                if (port < 0 || port > 65535) {
                    Toast.makeText(getContext(), R.string.printer_edit_toast_4, Toast.LENGTH_SHORT).show();
                    edtPort.setText(null);
                    InputMethodUtils.openInputMethod(edtPort);
                    return;
                }
            }
            if (edtIp.isFocused()) {
                InputMethodUtils.closeInputMethod(edtIp);
            }
            if (edtPort.isFocused()) {
                InputMethodUtils.closeInputMethod(edtPort);
            }
            if (executor == null) {
                executor = new PrintExecutor(ip, port, type);
                executor.setOnStateChangedListener(this);
                executor.setOnPrintResultListener(this);
            }
            executor.setIp(ip, port);
            //executor.doPrinterRequestAsync(maker);
            //executor.doPrinterRequest(maker);//同步打印.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    executor.doPrinterRequest(maker);
                }
            }).start();
        }

        @Override
        public void onStateChanged(int state) {
            switch (state) {
                case PrintSocketHolder.STATE_0:
                    dialog.setState(R.string.printer_test_message_1);
                    break;
                case PrintSocketHolder.STATE_1:
                    dialog.setState(R.string.printer_test_message_2);
                    break;
                case PrintSocketHolder.STATE_2:
                    dialog.setState(R.string.printer_test_message_3);
                    break;
                case PrintSocketHolder.STATE_3:
                    dialog.setState(R.string.printer_test_message_4);
                    break;
                case PrintSocketHolder.STATE_4:
                    dialog.setState(R.string.printer_test_message_5);
                    break;
            }
        }

        @Override
        public void onResult(int errorCode) {
            switch (errorCode) {
                case PrintSocketHolder.ERROR_0:
                    dialog.setState(R.string.printer_result_message_1);
                    break;
                case PrintSocketHolder.ERROR_1:
                    dialog.setState(R.string.printer_result_message_2);
                    break;
                case PrintSocketHolder.ERROR_2:
                    dialog.setState(R.string.printer_result_message_3);
                    break;
                case PrintSocketHolder.ERROR_3:
                    dialog.setState(R.string.printer_result_message_4);
                    break;
                case PrintSocketHolder.ERROR_4:
                    dialog.setState(R.string.printer_result_message_5);
                    break;
                case PrintSocketHolder.ERROR_5:
                    dialog.setState(R.string.printer_result_message_6);
                    break;
            }
            dialog.setEditable(true);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }

        @Override
        public void cancel() {
            super.cancel();
            if (executor != null)
                executor.closeSocket();
        }
    }

    public static IPTestCodeDgFrag getFragment(int type, int width, int height, String qr) {
        IPTestCodeDgFrag fragment = new IPTestCodeDgFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, type);
        bundle.putInt(EXTRA_WIDTH, width);
        bundle.putInt(EXTRA_HEIGHT, height);
        bundle.putString(EXTRA_QR, qr);
        fragment.setArguments(bundle);
        return fragment;
    }
}
