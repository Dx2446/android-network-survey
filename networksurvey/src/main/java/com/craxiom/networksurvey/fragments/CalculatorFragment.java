package com.craxiom.networksurvey.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.craxiom.networksurvey.R;
import com.craxiom.networksurvey.util.CalculationUtils;
import com.craxiom.networksurvey.util.CellularUtils;

import timber.log.Timber;

/**
 * A fragment to hold the LTE eNodeB ID calculator.
 *
 * @since 0.0.2
 */
public class CalculatorFragment extends Fragment
{
    static final String TITLE = "Calculators";
    private static final String INVALID_CELL_ID_MESSAGE = "Invalid Cell ID. Valid Range is 0 - 268435455";
    private static final String INVALID_PCI_MESSAGE = "Invalid PCI. Valid Range is 0 - 503";
    private static final String INVALID_EARFCN_MESSAGE = "Invalid EARFCN. Valid Range is 0 - 262143";

    private View view;

    private final TextWatcher lteCellIdTextWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            final String enteredText = s.toString();
            try
            {
                if (enteredText.isEmpty())
                {
                    Timber.v("The entered text for the LTE Cell ID is empty. Can't calculate the eNodeB ID.");
                    clearCellIdCalculatedValues();
                    return;
                }

                final int cellId;
                try
                {
                    cellId = Integer.parseInt(enteredText);
                } catch (Exception e)
                {
                    showToast(INVALID_CELL_ID_MESSAGE);
                    clearCellIdCalculatedValues();
                    return;
                }

                if (!CalculationUtils.isLteCellIdValid(cellId))
                {
                    showToast(INVALID_CELL_ID_MESSAGE);
                    clearCellIdCalculatedValues();
                    return;
                }

                // The Cell Identity is 28 bits long. The first 20 bits represent the Macro eNodeB ID. The last 8 bits
                // represent the sector.  Strip off the last 8 bits to get the Macro eNodeB ID.
                int eNodebId = CalculationUtils.getEnodebIdFromCellId(cellId);
                ((TextView) view.findViewById(R.id.calculatedEnbIdValue)).setText(String.valueOf(eNodebId));

                int sectorId = CalculationUtils.getSectorIdFromCellId(cellId);
                ((TextView) view.findViewById(R.id.calculatedSectorIdValue)).setText(String.valueOf(sectorId));
            } catch (Exception e)
            {
                Timber.w(e, "Unable to parse the provide LTE Cell ID as an Integer:%s", enteredText);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    private final TextWatcher ltePciTextWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            final String enteredText = s.toString();
            try
            {
                if (enteredText.isEmpty())
                {
                    Timber.v("The entered text for the LTE PCI is empty. Can't calculate the PSS and SSS.");
                    clearPciCalculatedValues();
                    return;
                }

                final int pci;
                try
                {
                    pci = Integer.parseInt(enteredText);
                } catch (Exception e)
                {
                    showToast(INVALID_PCI_MESSAGE);
                    clearPciCalculatedValues();
                    return;
                }

                if (pci < 0 || pci > 503)
                {
                    showToast(INVALID_PCI_MESSAGE);
                    clearPciCalculatedValues();
                    return;
                }

                int primarySyncSequence = CalculationUtils.getPrimarySyncSequence(pci);
                ((TextView) view.findViewById(R.id.calculatedPssValue)).setText(String.valueOf(primarySyncSequence));

                int secondarySyncSequence = CalculationUtils.getSecondarySyncSequence(pci);
                ((TextView) view.findViewById(R.id.calculatedSssValue)).setText(String.valueOf(secondarySyncSequence));
            } catch (Exception e)
            {
                Timber.w(e, "Unable to parse the provide LTE PCI as an Integer:%s", enteredText);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    private final TextWatcher lteEarfcnTextWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            final String enteredText = s.toString();
            try
            {
                if (enteredText.isEmpty())
                {
                    Timber.v("The entered text for the LTE EARFCN is empty. Can't calculate the Band.");
                    clearEarfcnCalculatedValues();
                    return;
                }

                final int earfcn;
                try
                {
                    earfcn = Integer.parseInt(enteredText);
                } catch (Exception e)
                {
                    showToast(INVALID_EARFCN_MESSAGE);
                    clearEarfcnCalculatedValues();
                    return;
                }

                if (earfcn < 0 || earfcn > 262143)
                {
                    showToast(INVALID_EARFCN_MESSAGE);
                    clearEarfcnCalculatedValues();
                    return;
                }

                int band = CellularUtils.downlinkEarfcnToBand(earfcn);
                if (band == -1)
                {
                    ((TextView) view.findViewById(R.id.calculatedBandValue)).setText("Unknown");
                } else
                {
                    ((TextView) view.findViewById(R.id.calculatedBandValue)).setText(String.valueOf(band));
                }
            } catch (Exception e)
            {
                Timber.w(e, "Unable to parse the provide LTE EARFCN as an Integer:%s", enteredText);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    public CalculatorFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calculator, container, false);

        final EditText cellIdField = view.findViewById(R.id.lteCalculatorCellId);
        cellIdField.addTextChangedListener(lteCellIdTextWatcher);

        final EditText pciField = view.findViewById(R.id.lteCalculatorPci);
        pciField.addTextChangedListener(ltePciTextWatcher);

        final EditText earfcnField = view.findViewById(R.id.lteCalculatorEarfcn);
        earfcnField.addTextChangedListener(lteEarfcnTextWatcher);

        return view;
    }

    /**
     * Sets the text in the Cell ID calculated TextView's to an empty string.
     */
    private void clearCellIdCalculatedValues()
    {
        ((TextView) view.findViewById(R.id.calculatedEnbIdValue)).setText("");
        ((TextView) view.findViewById(R.id.calculatedSectorIdValue)).setText("");
    }

    /**
     * Sets the text in the PCI calculated TextView's to an empty string.
     */
    private void clearPciCalculatedValues()
    {
        ((TextView) view.findViewById(R.id.calculatedPssValue)).setText("");
        ((TextView) view.findViewById(R.id.calculatedSssValue)).setText("");
    }

    /**
     * Sets the text in the Band calculated TextView to an empty string.
     */
    private void clearEarfcnCalculatedValues()
    {
        ((TextView) view.findViewById(R.id.calculatedBandValue)).setText("");
    }

    /**
     * Shows a short toast to the user with the provided message.
     * <p>
     * This method also logs a warning.
     *
     * @param toastMessage The message to show the user.
     */
    private void showToast(String toastMessage)
    {
        Timber.d(toastMessage);
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
