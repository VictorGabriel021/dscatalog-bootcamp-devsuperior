import React from 'react';
import Baseform from '../../BaseForm';
import './styles.scss';

const Form = () => {
  return (
    <Baseform title="CADASTRAR UM PRODUTO">
      <div className="row">
        <div className="col-6">
          <input type="text" className="form-control" />
        </div>
      </div>
    </Baseform>
  );
}

export default Form;